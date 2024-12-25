package com.ricky.yu.HabitTracker

import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.enums.Role
import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.models.HabitGroup
import com.ricky.yu.HabitTracker.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder


open class BaseTest {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    val testUser: User by lazy {
        User(id = 998L, email = "test@test.com", name = "test", password = passwordEncoder.encode("testPassword"), role = Role.USER)
    }

    val testGroup: HabitGroup by lazy {
        HabitGroup(id = 998L, name = "Test Group", testUser)
    }

    val testHabit: Habit by lazy {
        Habit(id = 998L, name = "Test Habit", description = "This is a test habit", frequency = Frequency.WEEKLY, user = testUser, group = testGroup)
    }
}