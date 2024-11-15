package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(email: String, rawPassword: String, name: String) {
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Email is already registered")
        }
        val hashedPassword = passwordEncoder.encode(rawPassword)
        val user = User(email = email, password = hashedPassword, name = name)
        userRepository.save(user)
    }
}
