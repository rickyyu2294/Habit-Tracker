package com.ricky.yu.habitTracker.models.compositeKeys

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class HabitGroupHabitKey(
    @Column(
        name = "habit_id",
        nullable = false,
    )
    val habitId: Long = 0,
    @Column(
        name = "habit_group_id",
        nullable = false,
    )
    val habitGroupId: Long = 0,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
