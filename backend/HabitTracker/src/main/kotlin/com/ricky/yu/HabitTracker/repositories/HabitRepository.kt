package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.Habit
import org.springframework.data.jpa.repository.JpaRepository

interface HabitRepository : JpaRepository<Habit, Long> {
    fun findByUserId(userId: Long): List<Habit>

    fun findByUserIdAndGroupId(userId: Long, groupId: Long): List<Habit>
}
