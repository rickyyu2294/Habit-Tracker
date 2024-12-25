package com.ricky.yu.HabitTracker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "habitCompletions",
    uniqueConstraints = [UniqueConstraint(columnNames = ["completion_date", "habit_id"])]
)
data class HabitCompletion (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    @JsonBackReference
    val habit: Habit,

    @Column(nullable = false)
    val completionDate: LocalDate
)