package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.models.Habit
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface HabitRepository : JpaRepository<Habit, Long> {
    fun findByUserId(userId: Long): List<Habit>

    fun existsByIdAndUserId(
        habitId: Long,
        userId: Long,
    ): Boolean

    fun findByIdAndUserId(
        id: Long,
        userId: Long,
    ): Optional<Habit>

    fun findByUserIdAndInterval(
        userId: Long,
        interval: IntervalType,
    ): List<Habit>
}
