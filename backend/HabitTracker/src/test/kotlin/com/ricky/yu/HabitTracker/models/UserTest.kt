package com.ricky.yu.HabitTracker.models

import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test

@SpringBootTest
class UserTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should create and retrieve user`() {
        val user = User(email = "test@test.com", name = "test", password = passwordEncoder.encode("password"))
        val savedUser = userRepository.save(user)

        assertNotNull(savedUser.id)
        assertEquals(user.email, savedUser.email)
        assertEquals(user.name, savedUser.name)
    }
}