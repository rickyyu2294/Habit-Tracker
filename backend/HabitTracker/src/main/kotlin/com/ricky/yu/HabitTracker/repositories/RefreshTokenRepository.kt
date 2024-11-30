package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
    fun deleteByToken(token: String): Int
}