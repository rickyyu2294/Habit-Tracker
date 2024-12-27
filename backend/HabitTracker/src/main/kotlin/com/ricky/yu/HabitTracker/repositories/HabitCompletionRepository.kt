package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.HabitCompletion
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface HabitCompletionRepository: JpaRepository<HabitCompletion, Long> {
    fun findByHabitId(habitId: Long): List<HabitCompletion>

    fun findByHabitIdAndCompletionDate(habitId: Long, completionDate: LocalDateTime): HabitCompletion?
}