package com.ricky.yu.HabitTracker.seeders

import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.enums.Role
import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
@Profile("dev", "test")
class DataSeeder(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Check if the test user already exists
        if (!userRepository.existsByEmail("test@test.com")) {
            // Create a test user
            val user = User(
                email = "test.user@test.com",
                name = "Test User",
                password = passwordEncoder.encode("testPassword"),
                role = Role.USER
            )

            val habit = Habit(
                name = "Test Habit",
                description = "Test habit description",
                frequency = Frequency.DAILY,
                user = user
            )
            userRepository.save(user)
            habitRepository.save(habit)
            println("Test user seeded: ${user.email}")
        } else {
            println("Test user already exists")
        }
    }
}