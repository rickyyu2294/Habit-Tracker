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

    fun deleteAllByHabitIdAndCompletionDateTimeIn(
        habitId: Long,
        dateTimes: List<LocalDateTime>,
    )
}
