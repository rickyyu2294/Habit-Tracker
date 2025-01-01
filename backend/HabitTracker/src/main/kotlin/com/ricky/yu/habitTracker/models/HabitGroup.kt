package com.ricky.yu.habitTracker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "habitGroups",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "user_id"])],
)
data class HabitGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val name: String,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    val user: User,
)