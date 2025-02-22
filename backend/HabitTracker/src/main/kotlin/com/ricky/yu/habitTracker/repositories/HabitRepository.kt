package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.models.Habit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    @Query("""
    SELECT h FROM Habit h 
    JOIN HabitGroupHabit hgh ON h.id = hgh.habit.id
    WHERE h.user.id = :userId 
    AND hgh.habitGroup.id = :groupId
    ORDER BY hgh.order
    """)
    fun findByUserIdAndGroupId(@Param("userId") userId: Long, @Param("groupId") groupId: Long): List<Habit>

}
