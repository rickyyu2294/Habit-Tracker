package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.context.RequestCtxHolder
import com.ricky.yu.HabitTracker.controllers.HabitController
import com.ricky.yu.HabitTracker.enums.Interval
import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.models.HabitGroup
import com.ricky.yu.HabitTracker.repositories.HabitGroupRepository
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Transactional
@Service
class HabitService(
    private val habitRepository: HabitRepository,
    private val habitGroupRepository: HabitGroupRepository,
    private val userService: UserService
) {
    fun createHabit(createRequest: HabitController.CreateHabitRequest): Habit {
        val ctx = RequestCtxHolder.getRequestContext()
        val user = userService.getUserById(ctx.userId)
        val habit = Habit(
            name = createRequest.name,
            description = createRequest.description,
            interval = parseInterval(createRequest),
            user = user,
            group = parseHabitGroup(createRequest)
        )
        return habitRepository.save(habit)
    }

    fun getHabitsForCurrentUser(): List<Habit> {
        val userId = RequestCtxHolder.getRequestContext().userId
        return habitRepository.findByUserId(userId)
    }

    fun getHabitsForCurrentUserAndGroup(groupId: Long): List<Habit> {
        val userId = RequestCtxHolder.getRequestContext().userId
        val group = habitGroupRepository.findById(groupId).get()
        if (userId == group.user.id) {
            return habitRepository.findByUserIdAndGroupId(userId, groupId)
        } else {
            throw IllegalArgumentException("User $userId does not own group $groupId")
        }
    }

    fun getHabitById(id: Long): Habit {
        val userId = RequestCtxHolder.getRequestContext().userId
        val habit = habitRepository.findByIdAndUserId(id, userId).orElseThrow()
        return habit
    }

    fun updateHabit(id: Long, updateRequest: HabitController.CreateHabitRequest): Habit {
        val existingHabit = getHabitById(id)

        // Find and validate the group if a groupId is provided
        val group = parseHabitGroup(updateRequest)

        // Create updated habit entity
        val updatedHabit = existingHabit.copy(
            name = updateRequest.name,
            description = updateRequest.description,
            interval = parseInterval(updateRequest),
            updatedAt = LocalDateTime.now(),
            group = group
        )

        // Save and return updated habit
        return habitRepository.save(updatedHabit)
    }

    fun deleteHabit(habitId: Long) {
        val habit = getHabitById(habitId)
        habitRepository.delete(habit)
    }

    fun validateUserOwnsHabit(habitId: Long) {
        val userId = RequestCtxHolder.getRequestContext().userId
        val isOwner = habitRepository.existsByIdAndUserId(habitId, userId)
        if (!isOwner) {
            throw IllegalArgumentException("User $userId does not own habit $habitId")
        }
    }

    // helpers
    // todo: maybe move these helpers
    private fun parseHabitGroup(createHabitRequest: HabitController.CreateHabitRequest): HabitGroup? {
        val group = createHabitRequest.groupId?.let {
            habitGroupRepository.findById(it).orElseThrow {
                NoSuchElementException("Habit group with ID $it not found")
            }
        }
        return group
    }

    private fun parseInterval(createHabitRequest: HabitController.CreateHabitRequest): Interval {
        val frequency = try {
            Interval.valueOf(createHabitRequest.interval.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid frequency: ${createHabitRequest.frequency}")
        }
        return frequency
    }
}