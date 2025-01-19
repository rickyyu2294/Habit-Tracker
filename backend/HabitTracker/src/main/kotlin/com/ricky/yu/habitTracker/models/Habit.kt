package com.ricky.yu.habitTracker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.ricky.yu.habitTracker.enums.IntervalType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
@Table(
    name = "habits",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "user_id"])],
)
data class Habit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val description: String = "",
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val interval: IntervalType,
    @Column(nullable = false)
    val frequency: Int = 1,
    // JSON representation of custom schedule
    @Column(nullable = true)
    val schedule: String? = null,
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    val user: User,
    @OneToMany(mappedBy = "habit", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    val completions: List<HabitCompletion> = listOf(),
    @OneToMany(
        mappedBy = "habit",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    val habitGroupHabits: MutableList<HabitGroupHabit> = mutableListOf(),
)
