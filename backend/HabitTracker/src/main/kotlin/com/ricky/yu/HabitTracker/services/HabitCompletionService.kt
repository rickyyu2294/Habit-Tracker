package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.enums.Interval
import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.repositories.HabitCompletionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Transactional
@Service
class HabitCompletionService(
    private val habitService: HabitService,
    private val habitCompletionRepository: HabitCompletionRepository
) {

    fun createCompletion(habitId: Long, date: LocalDateTime): HabitCompletion {
        val completion = habitCompletionRepository.findByHabitIdAndCompletionDateTime(habitId, date)
        if (completion != null) {
            throw IllegalArgumentException("Habit $habitId already complete for date $date")
        } else {
            val newCompletion = HabitCompletion(
                habit = habitService.getHabitById(habitId),
                completionDateTime = date
            )
            return habitCompletionRepository.save(newCompletion)
        }
    }

    fun getCompletions(habitId: Long): List<HabitCompletion> {
        habitService.validateUserOwnsHabit(habitId)
        return habitCompletionRepository.findByHabitId(habitId).sortedByDescending { it.completionDateTime }
    }

    fun getCompletionsGroupedByInterval(habitId: Long, frequency: Interval): Map<String, List<HabitCompletion>> {
        habitService.validateUserOwnsHabit(habitId)
        val completions = habitCompletionRepository.findByHabitId(habitId)

        return when (frequency) {
            Interval.DAILY -> completions.groupBy { completion -> completion.completionDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            Interval.WEEKLY -> completions.groupBy { completion -> completion.completionDateTime.format(DateTimeFormatter.ofPattern("yyyy-'W'ww")) }
            Interval.MONTHLY -> completions.groupBy { completion -> completion.completionDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) }
            else -> throw IllegalArgumentException("Unsupported interval: $frequency")
        }
    }

    fun deleteCompletion(habitId: Long, date: LocalDateTime) {
        habitService.validateUserOwnsHabit(habitId)

        val completion = habitCompletionRepository.findByHabitIdAndCompletionDateTime(habitId, date)
            ?: throw IllegalArgumentException("Habit $habitId has no completion for date $date")

        habitCompletionRepository.delete(completion)
    }

    fun deleteCompletions(habitId: Long, dateTimes: List<LocalDateTime>) {
        habitCompletionRepository.deleteAllByHabitIdAndCompletionDateTimeIn(habitId, dateTimes)
    }
}
