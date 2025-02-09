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

    @Query(
        """
        SELECT hc FROM HabitCompletion hc
        WHERE hc.habit.id = :habitId 
        AND hc.completionDateTime >= :startDate
        AND hc.completionDateTime < :endDate
        ORDER BY hc.completionDateTime DESC
        LIMIT 1
        """,
    )
    fun findLatestCompletionInRange(
        @Param("habitId") habitId: Long,
        @Param("startDate") startDateTime: LocalDateTime,
        @Param("endDate") endDateTime: LocalDateTime,
    ): HabitCompletion?

    @Query(
        """
        SELECT COUNT(hc) FROM HabitCompletion hc
        WHERE hc.habit.id = :habitId
        AND hc.completionDateTime >= :startDate
        AND hc.completionDateTime < :endDate
    """,
    )
    fun countCompletionsInRange(
        @Param("habitId") habitId: Long,
        @Param("startDate") startDateTime: LocalDateTime,
        @Param("endDate") endDateTime: LocalDateTime,
    ): Int
}
