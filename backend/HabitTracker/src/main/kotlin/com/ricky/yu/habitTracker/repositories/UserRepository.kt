package com.ricky.yu.habitTracker.repositories

import com.ricky.yu.habitTracker.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}
