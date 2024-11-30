package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.HabitCompletion
import org.springframework.data.jpa.repository.JpaRepository

interface HabitCompletionRepository: JpaRepository<HabitCompletion, Long> {
    fun findByHabitId(habitId: Long): List<HabitCompletion>
}