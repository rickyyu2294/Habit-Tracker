package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.HabitCompletion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    @Query("""
        SELECT hc FROM HabitCompletion hc
        WHERE hc.habit.id = :habitId 
        AND hc.completionDateTime BETWEEN :startDate AND :endDate
        ORDER BY hc.completionDateTime DESC
        """)
    fun findLatestCompletion(
        @Param("habitId") habitId: Long,
        @Param("startDate") startDateTime: LocalDateTime,
        @Param("endDate") endDateTime: LocalDateTime
    ): HabitCompletion?

    @Query("""
        SELECT COUNT(hc) FROM HabitCompletion hc
        WHERE hc.habit.id = :habitId
        AND hc.completionDateTime BETWEEN :startDate AND :endDate
    """)
    fun countCompletionsInRange(
        @Param("habitId") habitId: Long,
        @Param("startDate") startDateTime: LocalDateTime,
        @Param("endDate") endDateTime: LocalDateTime,
    ): Int
}
