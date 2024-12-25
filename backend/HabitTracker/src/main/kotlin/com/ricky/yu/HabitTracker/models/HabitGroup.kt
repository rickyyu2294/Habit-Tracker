package com.ricky.yu.HabitTracker.models

import jakarta.persistence.*

@Entity
@Table(
    name = "habitGroups",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "user_id"])]
)
data class HabitGroup (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)