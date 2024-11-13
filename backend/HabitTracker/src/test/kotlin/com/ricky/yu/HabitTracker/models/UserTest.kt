package com.ricky.yu.HabitTracker.models

import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class UserTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should create and retrieve user`() {
        val user = User(email = "test@test.com", name = "test")
        val savedUser = userRepository.save(user)

        assertNotNull(savedUser.id)
        assertEquals(user.email, savedUser.email)
        assertEquals(user.name, savedUser.name)
    }
}