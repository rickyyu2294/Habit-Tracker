package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.controllers.HabitController
import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.models.HabitGroup
import com.ricky.yu.HabitTracker.repositories.HabitGroupRepository
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import com.ricky.yu.HabitTracker.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Transactional
@Service
class HabitService(
    private val habitRepository: HabitRepository,
    private val userRepository: UserRepository,
    private val habitGroupRepository: HabitGroupRepository
) {
    fun createHabit(createRequest: HabitController.CreateHabitRequest): Habit {
        val user = userRepository.findByEmail(SecurityContextHolder.getContext().authentication.name)
            ?: throw NoSuchElementException("User not found")

        val habit = Habit(
            name = createRequest.name,
            description = createRequest.description,
            frequency = parseFrequency(createRequest),
            user = user,
            group = parseHabitGroup(createRequest)
        )
        return habitRepository.save(habit)
    }

    fun getAllHabitsForUser(userId: Long): List<Habit> {
        return habitRepository.findByUserId(userId)
    }

    fun getAllHabitsForUserForGroup(userId: Long, groupId: Long): List<Habit> {
        // validate group is owned by user
        val group = habitGroupRepository.findById(groupId).get()
        if (userId == group.user.id) {
            return habitRepository.findByUserIdAndGroupId(userId, groupId)
        } else {
            throw IllegalArgumentException("User $userId does not own group $groupId")
        }
    }

    fun getHabitById(id: Long): Habit {
        return habitRepository.findById(id)
            .orElseThrow { NoSuchElementException("Habit not found with id: $id") }
    }

    fun updateHabit(id: Long, updateRequest: HabitController.CreateHabitRequest): Habit {
        val existingHabit = getHabitById(id)

        // Find and validate the group if a groupId is provided
        val group = parseHabitGroup(updateRequest)

        // Create updated habit entity
        val updatedHabit = existingHabit.copy(
            name = updateRequest.name,
            description = updateRequest.description,
            frequency = parseFrequency(updateRequest),
            updatedAt = LocalDateTime.now(),
            group = group
        )

        // Save and return updated habit
        return habitRepository.save(updatedHabit)
    }

    fun deleteHabit(id: Long) {
        habitRepository.deleteById(id)
    }

    // helpers

    private fun parseHabitGroup(createHabitRequest: HabitController.CreateHabitRequest): HabitGroup? {
        val group = createHabitRequest.groupId?.let {
            habitGroupRepository.findById(it).orElseThrow {
                NoSuchElementException("Habit group with ID $it not found")
            }
        }
        return group
    }

    private fun parseFrequency(createHabitRequest: HabitController.CreateHabitRequest): Frequency {
        val frequency = try {
            Frequency.valueOf(createHabitRequest.frequency.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid frequency: ${createHabitRequest.frequency}")
        }
        return frequency
    }
}