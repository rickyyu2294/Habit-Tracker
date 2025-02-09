package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.context.RequestCtxHolder
import com.ricky.yu.habitTracker.controllers.HabitController
import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.models.Habit
import com.ricky.yu.habitTracker.models.HabitGroup
import com.ricky.yu.habitTracker.models.HabitGroupHabit
import com.ricky.yu.habitTracker.models.compositeKeys.HabitGroupHabitKey
import com.ricky.yu.habitTracker.repositories.HabitGroupHabitRepository
import com.ricky.yu.habitTracker.repositories.HabitRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Transactional
@Service
class HabitService(
    private val habitRepository: HabitRepository,
    private val userService: UserService,
    private val habitGroupService: HabitGroupService,
    private val habitGroupHabitRepository: HabitGroupHabitRepository,
) {
    fun createHabit(createRequest: HabitController.CreateHabitRequest): Habit {
        val user = userService.getUserById(RequestCtxHolder.get().userId)
        val groups = parseHabitGroups(createRequest)

        var habit =
            Habit(
                name = createRequest.name,
                description = createRequest.description,
                interval = parseInterval(createRequest),
                frequency = createRequest.frequency,
                user = user,
            )
        habit = habitRepository.save(habit)
        updateHabitGroups(
            habit = habit,
            updatedGroups = groups,
            allowSystemGeneratedGroupsEdit = true,
        )
        return habit
    }

    fun getHabitsForCurrentUser(
        interval: IntervalType? = null,
        groupId: Long? = null,
    ): List<Habit> {
        val userId = RequestCtxHolder.get().userId
        var habits =
            if (interval != null) {
                habitRepository.findByUserIdAndInterval(userId, interval)
            } else {
                habitRepository.findByUserId(userId)
            }

        if (groupId != null) {
            val habitIds = habitGroupHabitRepository.findByHabitGroup_IdOrderByOrderAsc(groupId).map { it.habit.id }
            habits = habits.filter { it.id in habitIds }
        }

        return habits
    }

    fun getHabitById(id: Long): Habit {
        val userId = RequestCtxHolder.get().userId
        val habit = habitRepository.findByIdAndUserId(id, userId).orElseThrow()
        return habit
    }

    fun updateHabit(
        id: Long,
        updateRequest: HabitController.CreateHabitRequest,
    ): Habit {
        val existingHabit = getHabitById(id)

        val updatedGroups = parseHabitGroups(updateRequest)

        // Create updated habit entity
        val updatedHabit =
            existingHabit.copy(
                name = updateRequest.name,
                description = updateRequest.description,
                interval = parseInterval(updateRequest),
                updatedAt = LocalDateTime.now(),
            )

        updateHabitGroups(updatedHabit, updatedGroups, true)

        // Save and return updated habit
        return habitRepository.save(updatedHabit)
    }

    fun deleteHabit(habitId: Long) {
        val habit = getHabitById(habitId)
        habitRepository.delete(habit)
    }

    fun validateUserOwnsHabit(habitId: Long) {
        val userId = RequestCtxHolder.get().userId
        val isOwner = habitRepository.existsByIdAndUserId(habitId, userId)
        require(isOwner) { throw IllegalArgumentException("User $userId does not own habit $habitId") }
    }

    // helpers
    // maybe move these helpers
    private fun parseHabitGroups(createHabitRequest: HabitController.CreateHabitRequest): List<HabitGroup> {
        // Ensure the groupsId include the system generated group 'All'
        val allGroup = habitGroupService.getGroupByName("All")

        val groups =
            createHabitRequest.groupIds.map { groupId ->
                habitGroupService.getGroupById(groupId)
            }.toMutableList()

        if (!groups.contains(allGroup)) groups.add(allGroup)
        return groups
    }

    private fun parseInterval(createHabitRequest: HabitController.CreateHabitRequest): IntervalType {
        val frequency =
            try {
                IntervalType.valueOf(createHabitRequest.interval.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid interval: ${createHabitRequest.frequency}", e)
            }
        return frequency
    }

    fun updateHabitGroups(
        habit: Habit,
        updatedGroups: List<HabitGroup>,
        allowSystemGeneratedGroupsEdit: Boolean = false,
    ) {
        val currentGroups = habit.habitGroupHabits.map { it.habitGroup }
        val groupsToAdd = updatedGroups - currentGroups.toSet()
        val groupsToRemove = currentGroups - updatedGroups.toSet()

        // Remove associations
        groupsToRemove.forEach { group ->
            // validate group is not system generated
            if (!allowSystemGeneratedGroupsEdit) {
                require(!habitGroupService.isSystemGenerated(group.id)) {
                    "Cannot remove habit from system generated group: group id ${group.id}"
                }
            }
            habit.habitGroupHabits.removeIf { it.habitGroup == group }
        }

        // Add new associations
        groupsToAdd.forEach { group ->
            if (!allowSystemGeneratedGroupsEdit) {
                require(!habitGroupService.isSystemGenerated(group.id)) {
                    "Cannot add habit to system generated group: group id ${group.id}"
                }
            }
            val habitGroupHabit =
                HabitGroupHabit(
                    id = HabitGroupHabitKey(habitId = habit.id, habitGroupId = group.id),
                    habit = habit,
                    habitGroup = group,
                    order = habitGroupService.getLastOrderInGroup(group.id) + 1,
                )
            habit.habitGroupHabits.add(habitGroupHabit)
        }
    }
}
