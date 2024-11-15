package com.ricky.yu.HabitTracker.repositories

import com.ricky.yu.HabitTracker.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}
