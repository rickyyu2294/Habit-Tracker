package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.context.RequestCtxHolder
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

    fun markCompletion(habitId: Long, date: LocalDate): HabitCompletion {
        val completion = habitCompletionRepository.findByHabitIdAndCompletionDate(habitId, date)
        // todo: customize calculation of habit completion by frequency
        if (completion != null) {
            throw IllegalArgumentException("Habit $habitId already complete for date $date")
        } else {
            val newCompletion = HabitCompletion(
                habit = habitService.getHabitById(habitId),
                completionDate = date
            )
            return habitCompletionRepository.save(newCompletion)
        }
    }

    fun getCompletionHistory(habitId: Long): List<HabitCompletion> {
        habitService.validateUserOwnsHabit(habitId)
        return habitCompletionRepository.findByHabitId(habitId).sortedByDescending { it.completionDate }
    }

    fun deleteCompletion(habitId: Long, date: LocalDate) {
        habitService.validateUserOwnsHabit(habitId)

        val completion = habitCompletionRepository.findByHabitIdAndCompletionDate(habitId, date)
            ?: throw IllegalArgumentException("Habit $habitId has no completion for date $date")

        habitCompletionRepository.delete(completion)
    }
}
