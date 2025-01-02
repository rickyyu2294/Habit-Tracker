package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.HabitCompletion
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface HabitCompletionRepository : JpaRepository<HabitCompletion, Long> {
    fun findByHabitId(habitId: Long): List<HabitCompletion>

    fun findByHabitIdAndCompletionDateTime(
        habitId: Long,
        completionDateTIme: LocalDateTime,
    ): HabitCompletion?

    fun findByHabitIdAndId(
        habitId: Long,
        id: Long,
    ): HabitCompletion?

    fun deleteAllByHabitIdAndIdIn(
        habitId: Long,
        ids: List<Long>,
    )

    fun findTopByHabitIdAndCompletionDateTimeBetweenOrderByCompletionDateTimeDesc(
        habitId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): HabitCompletion?
}
