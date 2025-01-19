package com.ricky.yu.habitTracker.models

import com.ricky.yu.habitTracker.models.compositeKeys.HabitGroupHabitKey
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId

@Entity
data class HabitGroupHabit(
    @EmbeddedId
    val id: HabitGroupHabitKey,
    @ManyToOne
    @MapsId("habitId")
    @JoinColumn(name = "habit_id")
    val habit: Habit,
    @ManyToOne
    @MapsId("habitGroupId")
    @JoinColumn(name = "habit_group_id")
    val habitGroup: HabitGroup,
    @Column(name = "sort_order", nullable = false)
    var order: Int = 0,
)
