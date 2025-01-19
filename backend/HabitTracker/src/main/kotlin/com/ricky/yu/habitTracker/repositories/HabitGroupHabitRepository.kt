package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.HabitGroupHabit
import com.ricky.yu.habitTracker.models.compositeKeys.HabitGroupHabitKey
import org.springframework.data.jpa.repository.JpaRepository

interface HabitGroupHabitRepository : JpaRepository<HabitGroupHabit, HabitGroupHabitKey> {
    @Suppress("FunctionNaming", "ktlint:standard:function-naming")
    fun findByHabitGroup_IdOrderByOrderAsc(groupId: Long): List<HabitGroupHabit>
}
