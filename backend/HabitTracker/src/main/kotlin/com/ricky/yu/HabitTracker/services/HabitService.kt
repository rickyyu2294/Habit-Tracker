package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Transactional
@Service
class HabitService(
    private val habitRepository: HabitRepository
) {
    fun createHabit(habit: Habit): Habit {
        return habitRepository.save(habit)
    }

    fun getAllHabitsForUser(userId: Long): List<Habit> {
        return habitRepository.findByUserId(userId)
    }

    fun getHabitById(id: Long): Habit {
        return habitRepository.findById(id)
            .orElseThrow { NoSuchElementException("Habit not found with id: $id") }
    }

    fun updateHabit(id: Long, updateHabit: Habit): Habit {
        val existingHabit = getHabitById(id)
        return habitRepository.save(
            existingHabit.copy(
                name = updateHabit.name,
                description = updateHabit.description,
                frequency = updateHabit.frequency,
                updatedAt = LocalDateTime.now()
            )
        )
    }

    fun deleteHabit(id: Long) {
        habitRepository.deleteById(id)
    }
}