package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.controllers.HabitController
import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.models.Habit
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
    private val userRepository: UserRepository
) {
    fun createHabit(input: HabitController.CreateHabitRequest): Habit {
        val user = userRepository.findByEmail(SecurityContextHolder.getContext().authentication.name)
            ?: throw NoSuchElementException("User not found")

        val habit = Habit(
            name = input.name,
            description = input.description,
            frequency = Frequency.valueOf(input.frequency.uppercase()),
            user = user
        )
        return habitRepository.save(habit)
    }

    fun getAllHabitsForUser(userId: Long): List<Habit> {
        return habitRepository.findByUserId(userId)
    }

    fun getHabitById(id: Long): Habit {
        return habitRepository.findById(id)
            .orElseThrow { NoSuchElementException("Habit not found with id: $id") }
    }

    fun updateHabit(id: Long, updateHabit: HabitController.CreateHabitRequest): Habit {
        val existingHabit = getHabitById(id)
        return habitRepository.save(
            existingHabit.copy(
                name = updateHabit.name,
                description = updateHabit.description,
                frequency = Frequency.valueOf(updateHabit.frequency.uppercase()),
                updatedAt = LocalDateTime.now()
            )
        )
    }

    fun deleteHabit(id: Long) {
        habitRepository.deleteById(id)
    }
}