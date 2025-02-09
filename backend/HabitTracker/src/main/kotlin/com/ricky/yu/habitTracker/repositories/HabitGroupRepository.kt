package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.HabitGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    fun countById(groupId: Long): Int

    @Query("SELECT hg.isSystemGenerated FROM HabitGroup hg WHERE hg.id = :groupId")
    fun findIsSystemGeneratedByGroupId(
        @Param("groupId") groupId: Long,
    ): Optional<Boolean>
}
