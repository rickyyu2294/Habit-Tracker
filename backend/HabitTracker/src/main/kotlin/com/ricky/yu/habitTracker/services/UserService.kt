package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.enums.Role
import com.ricky.yu.habitTracker.eventListeners.UserRegisteredEvent
import com.ricky.yu.habitTracker.models.User
import com.ricky.yu.habitTracker.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Transactional
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun registerUser(
        email: String,
        rawPassword: String,
        name: String,
    ): User {
        require(!userRepository.existsByEmail(email)) { "Email is already registered" }
        val hashedPassword = passwordEncoder.encode(rawPassword)
        val user = userRepository.save(User(email = email, password = hashedPassword, name = name, role = Role.USER))

        applicationEventPublisher.publishEvent(UserRegisteredEvent(user))

        return getUserById(user.id)
    }

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow()
    }

    fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserByEmail(userEmail: String): User {
        return userRepository.findByEmail(userEmail).orElseThrow()
    }
}
