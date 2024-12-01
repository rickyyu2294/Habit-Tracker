package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.repositories.HabitCompletionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Transactional
@Service
class HabitCompletionService(
    private val habitService: HabitService,
    private val habitCompletionRepository: HabitCompletionRepository
) {
    fun markComplete(habitId: Long, date: LocalDate): HabitCompletion {
        val habit = habitService.getHabitById(habitId)
        val completion = HabitCompletion(habit = habit, completionDate = date)
        return habitCompletionRepository.save(completion)
    }

    fun getCompletionHistory(habitId: Long): List<HabitCompletion> {
        return habitCompletionRepository.findByHabitId(habitId).sortedByDescending { it.completionDate }
    }
}
