package com.ricky.yu.HabitTracker.models

import com.fasterxml.jackson.annotation.JsonBackReference
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

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    val user: User
)