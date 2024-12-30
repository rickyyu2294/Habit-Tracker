package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.HabitGroup
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface HabitGroupRepository : JpaRepository<HabitGroup, Long> {
    fun findAllByUserId(id: Long): List<HabitGroup>

    fun existsByIdAndUserId(
        groupId: Long,
        userId: Long,
    ): Boolean

    fun findByIdAndUserId(
        groupId: Long,
        userId: Long,
    ): Optional<HabitGroup>
}
