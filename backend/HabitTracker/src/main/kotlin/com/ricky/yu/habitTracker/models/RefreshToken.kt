package com.ricky.yu.habitTracker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "refreshTokens")
data class RefreshToken(
    @Id
    val token: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    val user: User,
)
