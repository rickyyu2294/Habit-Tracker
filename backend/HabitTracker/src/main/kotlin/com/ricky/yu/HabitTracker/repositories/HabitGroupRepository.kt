package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.HabitGroup
import org.springframework.data.jpa.repository.JpaRepository

interface HabitGroupRepository: JpaRepository<HabitGroup, Long> {
    fun findAllByUserId(id: Long): List<HabitGroup>
}