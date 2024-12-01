package com.ricky.yu.HabitTracker

import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.enums.Role
import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.models.User
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class BaseTest {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    val testUser: User by lazy {
        User(id = 1L, email = "test@test.com", name = "test", password = passwordEncoder.encode("testPassword"), role = Role.USER)
    }

    val testHabit: Habit by lazy {
        Habit(id = 1L, name = "Test Habit", description = "This is a test habit", frequency = Frequency.WEEKLY, user = testUser)
    }
}