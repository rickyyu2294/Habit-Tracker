package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.models.HabitCompletion
import com.ricky.yu.habitTracker.repositories.HabitCompletionRepository
import com.ricky.yu.habitTracker.utils.IntervalUtils
import com.ricky.yu.habitTracker.utils.IntervalUtils.dailyFormatter
import com.ricky.yu.habitTracker.utils.IntervalUtils.monthlyFormatter
import com.ricky.yu.habitTracker.utils.IntervalUtils.weeklyFormatter
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

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
                    completion.completionDateTime.format(dailyFormatter)
                }
            IntervalType.WEEKLY ->
                completions.groupBy {
                        completion ->
                    completion.completionDateTime.format(weeklyFormatter)
                }
            IntervalType.MONTHLY ->
                completions.groupBy {
                        completion ->
                    completion.completionDateTime.format(monthlyFormatter)
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

    fun decrementCompletion(
        habitId: Long,
        interval: String,
    ) {
        val habit = habitService.getHabitById(habitId)
        require(habit.interval in IntervalType.entries)

        val (startDateTime, endDateTime) =
            when (habit.interval) {
                IntervalType.DAILY -> {
                    IntervalUtils.dailyIntervalToRange(interval)
                }
                IntervalType.WEEKLY -> {
                    IntervalUtils.weeklyIntervalToRange(interval)
                }
                IntervalType.MONTHLY -> {
                    IntervalUtils.monthlyIntervalToRange(interval)
                }

                IntervalType.YEARLY -> TODO()
            }

        val latestCompletion =
            habitCompletionRepository
                .findTopByHabitIdAndCompletionDateTimeBetweenOrderByCompletionDateTimeDesc(
                    habitId,
                    startDateTime,
                    endDateTime,
                )
        requireNotNull(latestCompletion) {
            "No completion in range $startDateTime and $endDateTime" +
                " for interval $interval of habit $habitId"
        }

        habitCompletionRepository.delete(latestCompletion)
    }
}
