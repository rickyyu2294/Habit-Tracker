package com.ricky.yu.HabitTracker

import com.ricky.yu.HabitTracker.enums.Role
import com.ricky.yu.HabitTracker.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class BaseTest {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    val testUser: User by lazy {
        User(email = "test@test.com", name = "test", password = passwordEncoder.encode("testPassword"), role = Role.USER)
    }
}