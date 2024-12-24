package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByToken(token: String): Int
}