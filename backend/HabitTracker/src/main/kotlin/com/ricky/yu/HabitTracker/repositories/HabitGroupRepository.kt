package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.HabitGroup
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HabitGroupRepository: JpaRepository<HabitGroup, Long> {
    fun findAllByUserId(id: Long): List<HabitGroup>
    fun existsByIdAndUserId(groupId: Long, userId: Long): Boolean
    fun findByIdAndUserId(groupId: Long, userId: Long): Optional<HabitGroup>
}