package com.ricky.yu.HabitTracker.models

import jakarta.persistence.*

@Entity
@Table(name = "refreshTokens")
data class RefreshToken(
    @Id
    val token: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)