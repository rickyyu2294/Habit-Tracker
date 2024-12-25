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

    data class MarkOrRetrieveCompletionResult(val completion: HabitCompletion, val isNewlyCreated: Boolean)
    fun markOrRetrieveCompletion(id: Long, date: LocalDate): MarkOrRetrieveCompletionResult {
        val completion = habitCompletionRepository.findByHabitIdAndCompletionDate(id, date)
        return completion?.let {
            MarkOrRetrieveCompletionResult(completion, false)
        } ?: run {
            val habit = habitService.getHabitById(id)
            val newCompletion = HabitCompletion(habit = habit, completionDate = date)
            MarkOrRetrieveCompletionResult(habitCompletionRepository.save(newCompletion), true)
        }
    }

    fun getCompletionHistory(habitId: Long): List<HabitCompletion> {
        // todo: ensure habit is owned by authenticated user
        return habitCompletionRepository.findByHabitId(habitId).sortedByDescending { it.completionDate }
    }

    fun deleteCompletion(id: Long, date: LocalDate) {
        val completion = habitCompletionRepository.findByHabitIdAndCompletionDate(id, date)
            ?: throw IllegalArgumentException("Habit $id has no completion for date $date")

        habitCompletionRepository.delete(completion)
    }
}
