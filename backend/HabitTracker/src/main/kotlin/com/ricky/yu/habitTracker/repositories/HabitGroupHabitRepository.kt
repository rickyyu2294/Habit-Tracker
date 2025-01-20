package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.HabitGroupHabit
import com.ricky.yu.habitTracker.models.compositeKeys.HabitGroupHabitKey
import org.springframework.data.jpa.repository.JpaRepository

@Suppress("FunctionNaming", "ktlint:standard:function-naming")
interface HabitGroupHabitRepository : JpaRepository<HabitGroupHabit, HabitGroupHabitKey> {
    fun findByHabitGroup_IdOrderByOrderAsc(groupId: Long): List<HabitGroupHabit>
    fun findTopByHabitGroup_IdOrderByOrderDesc(groupId: Long): HabitGroupHabit?
}
