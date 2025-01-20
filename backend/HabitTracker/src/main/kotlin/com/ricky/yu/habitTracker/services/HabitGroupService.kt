package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.context.RequestCtxHolder
import com.ricky.yu.habitTracker.models.Habit
import com.ricky.yu.habitTracker.models.HabitGroup
import com.ricky.yu.habitTracker.models.HabitGroupHabit
import com.ricky.yu.habitTracker.models.compositeKeys.HabitGroupHabitKey
import com.ricky.yu.habitTracker.repositories.HabitGroupHabitRepository
import com.ricky.yu.habitTracker.repositories.HabitGroupRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class HabitGroupService(
    private val habitGroupRepository: HabitGroupRepository,
    private val userService: UserService,
    private val habitGroupHabitRepository: HabitGroupHabitRepository,
) {
    fun createGroup(name: String): HabitGroup {
        val userId = RequestCtxHolder.getRequestContext().userId
        val habitGroup =
            HabitGroup(
                name = name,
                user = userService.getUserById(userId),
            )
        return habitGroupRepository.save(habitGroup)
    }

    fun deleteGroup(groupId: Long) {
        val group = getGroupById(groupId)
        habitGroupRepository.delete(group)
    }

    fun renameGroup(
        groupId: Long,
        newName: String,
    ): HabitGroup {
        val group = getGroupById(groupId)

        val updatedGroup = group.copy(name = newName)
        return habitGroupRepository.save(updatedGroup)
    }

    fun getGroups(): List<HabitGroup> {
        val userId = RequestCtxHolder.getRequestContext().userId
        return habitGroupRepository.findAllByUserId(userId)
    }

    fun getGroupById(groupId: Long): HabitGroup {
        val userId = RequestCtxHolder.getRequestContext().userId
        val group =
            habitGroupRepository.findByIdAndUserId(groupId, userId).orElseThrow {
                NoSuchElementException("No habit group $groupId for user $userId")
            }
        return group
    }

    fun validateUserOwnsHabitGroup(groupId: Long) {
        val ctx = RequestCtxHolder.getRequestContext()
        val userId = ctx.userId
        val isOwner = habitGroupRepository.existsByIdAndUserId(groupId, userId)
        require(isOwner) { "User $userId does not own group $groupId" }
    }

    fun reorderGroup(groupId: Long) {
        // get all habitGroupHabits for group, sorted asc by order
        // and reset ordering starting from 0
        val groupHabits = habitGroupHabitRepository.findByHabitGroup_IdOrderByOrderAsc(groupId)
        var index = 0
        groupHabits.forEach { habit ->
            habit.order = index++
        }
    }

    fun getGroupSize(groupId: Long): Int {
        validateUserOwnsHabitGroup(groupId)
        return habitGroupRepository.countById(groupId)
    }

    fun syncHabitGroups(
        habit: Habit,
        updatedGroups: Set<HabitGroup>,
    ) {
        val currentGroups = habit.habitGroupHabits.map { it.habitGroup }.toSet()

        val groupsToAdd = updatedGroups - currentGroups
        val groupsToRemove = currentGroups - updatedGroups

        // Remove old group associations
        groupsToRemove.forEach { group ->
            habit.habitGroupHabits.find { it.habitGroup == group }?.let {
                habit.habitGroupHabits.remove(it)
            }
            reorderGroup(group.id)
        }

        // Add new group associations
        groupsToAdd.forEach { group ->
            val newMapping =
                HabitGroupHabit(
                    id = HabitGroupHabitKey(habitId = habit.id, habitGroupId = group.id),
                    habit = habit,
                    habitGroup = group,
                    // Default or user-defined
                    order = getGroupSize(group.id) + 1,
                )
            habit.habitGroupHabits.add(newMapping)
        }
    }
}
