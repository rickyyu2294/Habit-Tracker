package com.ricky.yu.HabitTracker.models

import com.ricky.yu.HabitTracker.enums.Frequency
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "habits")
data class Habit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val frequency: Frequency,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(mappedBy = "habit", cascade = [CascadeType.ALL], orphanRemoval = true)
    val completions: List<HabitCompletion> = mutableListOf()
)