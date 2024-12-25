package com.ricky.yu.HabitTracker.utils

import com.ricky.yu.HabitTracker.models.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtil {
    fun getAuthenticatedUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authenticated user found")

        val userDetails = authentication.principal as User // Or your UserDetails implementation
        return userDetails // Map to your User entity
    }
}