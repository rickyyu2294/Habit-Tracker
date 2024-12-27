package com.ricky.yu.HabitTracker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.ricky.yu.HabitTracker.enums.Interval
import jakarta.persistence.*
import jdk.jfr.Frequency
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
@Table(
    name = "habits",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "user_id"])]
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
    val interval: Interval,

    @Column(nullable = false)
    val frequency: Int = 1,

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

    @ManyToOne
    @JoinColumn(name = "group_id")
    val group: HabitGroup? = null
) {

}