package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.models.HabitCompletion
import com.ricky.yu.habitTracker.repositories.HabitCompletionRepository
import com.ricky.yu.habitTracker.utils.IntervalUtils.dailyFormatter
import com.ricky.yu.habitTracker.utils.IntervalUtils.getEarliestDateTimeInDailyInterval
import com.ricky.yu.habitTracker.utils.IntervalUtils.getEarliestDateTimeInMonthlyInterval
import com.ricky.yu.habitTracker.utils.IntervalUtils.getEarliestDateTimeInWeeklyInterval
import com.ricky.yu.habitTracker.utils.IntervalUtils.intervalToStartAndEndTime
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
        dateTime: LocalDateTime,
    ): HabitCompletion {
        habitService.validateUserOwnsHabit(habitId)
        val newCompletion =
            HabitCompletion(
                habit = habitService.getHabitById(habitId),
                completionDateTime = dateTime,
            )
        return habitCompletionRepository.save(newCompletion)
    }

    fun createCompletionInInterval(
        habitId: Long,
        interval: String
    ): HabitCompletion {
        val habit = habitService.getHabitById(habitId)

        check(!isIntervalFullyComplete(habitId, interval))

        val dateTime = when (habit.interval) {
            IntervalType.DAILY -> {
                getEarliestDateTimeInDailyInterval(interval)
            }
            IntervalType.WEEKLY -> {
                getEarliestDateTimeInWeeklyInterval(interval)
            }
            IntervalType.MONTHLY -> {
                getEarliestDateTimeInMonthlyInterval(interval)
            }
            else -> throw IllegalArgumentException("Unsupported interval: $interval")
        }

        return createCompletion(habitId, dateTime)
    }

    private fun isIntervalFullyComplete(habitId: Long, interval: String): Boolean {
        val habit = habitService.getHabitById(habitId)
        val (startDateTime, endDateTime) = intervalToStartAndEndTime(habit.interval, interval)

        val completionCount = habitCompletionRepository.countByHabitIdAndCompletionDateTimeGreaterThanEqualAndCompletionDateTimeLessThan(habitId, startDateTime, endDateTime)
        return completionCount >= habit.frequency
    }

    fun getCompletions(habitId: Long): List<HabitCompletion> {
        habitService.validateUserOwnsHabit(habitId)
        return habitCompletionRepository.findByHabitId(habitId).sortedByDescending { it.completionDateTime }
    }

    fun getCompletionsGroupedByInterval(
        habitId: Long,
        intervalType: IntervalType,
    ): Map<String, List<HabitCompletion>> {
        habitService.validateUserOwnsHabit(habitId)
        val completions = habitCompletionRepository.findByHabitId(habitId)

        return when (intervalType) {
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
            else -> throw IllegalArgumentException("Unsupported interval: $intervalType")
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
            intervalToStartAndEndTime(habit.interval, interval)

        val latestCompletion =
            habitCompletionRepository
                .findTopByHabitIdAndCompletionDateTimeGreaterThanEqualAndCompletionDateTimeLessThanOrderByCompletionDateTimeDesc(
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
