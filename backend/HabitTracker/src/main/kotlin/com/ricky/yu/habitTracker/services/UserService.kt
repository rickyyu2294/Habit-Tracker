package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.enums.Role
import com.ricky.yu.habitTracker.models.User
import com.ricky.yu.habitTracker.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun registerUser(
        email: String,
        rawPassword: String,
        name: String,
    ) {
        require(userRepository.existsByEmail(email)) { "Email is already registered" }
        val hashedPassword = passwordEncoder.encode(rawPassword)
        val user = User(email = email, password = hashedPassword, name = name, role = Role.USER)
        userRepository.save(user)
    }

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow()
    }
}
