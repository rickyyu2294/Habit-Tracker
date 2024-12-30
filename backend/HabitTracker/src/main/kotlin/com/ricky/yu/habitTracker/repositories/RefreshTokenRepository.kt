package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {
    fun findByToken(token: String): Optional<RefreshToken>

    fun deleteByToken(token: String): Int
}
