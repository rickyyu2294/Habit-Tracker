package com.ricky.yu.HabitTracker.models

import com.ricky.yu.HabitTracker.BaseTest
import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest: BaseTest() {
    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun `should create and retrieve user`() {
        val user = testUser
        val savedUser = userRepository.save(user)

        assertNotNull(savedUser.id)
        assertEquals(user.email, savedUser.email)
        assertEquals(user.name, savedUser.name)
    }
}