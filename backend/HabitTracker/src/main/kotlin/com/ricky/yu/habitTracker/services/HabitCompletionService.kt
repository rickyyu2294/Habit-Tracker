package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.models.HabitCompletion
import com.ricky.yu.habitTracker.repositories.HabitCompletionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Transactional
@Service
class HabitCompletionService(
    private val habitService: HabitService,
    private val habitCompletionRepository: HabitCompletionRepository,
) {
    fun createCompletion(
        habitId: Long,
        date: LocalDateTime,
    ): HabitCompletion {
        val completion = habitCompletionRepository.findByHabitIdAndCompletionDateTime(habitId, date)
        requireNotNull(completion) { "Habit $habitId already complete for date $date" }
        val newCompletion =
            HabitCompletion(
                habit = habitService.getHabitById(habitId),
                completionDateTime = date,
            )
        return habitCompletionRepository.save(newCompletion)
    }

    fun getCompletions(habitId: Long): List<HabitCompletion> {
        habitService.validateUserOwnsHabit(habitId)
        return habitCompletionRepository.findByHabitId(habitId).sortedByDescending { it.completionDateTime }
    }

    fun getCompletionsGroupedByInterval(
        habitId: Long,
        frequency: IntervalType,
    ): Map<String, List<HabitCompletion>> {
        habitService.validateUserOwnsHabit(habitId)
        val completions = habitCompletionRepository.findByHabitId(habitId)

        return when (frequency) {
            IntervalType.DAILY ->
                completions.groupBy {
                        completion ->
                    completion.completionDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                }
            IntervalType.WEEKLY ->
                completions.groupBy {
                        completion ->
                    completion.completionDateTime.format(DateTimeFormatter.ofPattern("yyyy-'W'ww"))
                }
            IntervalType.MONTHLY ->
                completions.groupBy {
                        completion ->
                    completion.completionDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                }
            else -> throw IllegalArgumentException("Unsupported interval: $frequency")
        }
    }

    fun deleteCompletion(
        habitId: Long,
        id: Long,
    ) {
        habitService.validateUserOwnsHabit(habitId)

        val completion: HabitCompletion =
            habitCompletionRepository.findByHabitIdAndId(habitId, id)
                ?: throw IllegalArgumentException("Habit $habitId has no completion $id")

        habitCompletionRepository.delete(completion)
    }

    fun deleteCompletions(
        habitId: Long,
        ids: List<Long>,
    ) {
        habitCompletionRepository.deleteAllByHabitIdAndIdIn(habitId, ids)
    }
}
