package com.ricky.yu.HabitTracker.seeders

import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.enums.Role
import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.models.HabitGroup
import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.repositories.HabitCompletionRepository
import com.ricky.yu.HabitTracker.repositories.HabitGroupRepository
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@Profile("dev", "test")
class DataSeeder(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository,
    private val habitCompletionRepository: HabitCompletionRepository,
    private val groupRepository: HabitGroupRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Check if the test user already exists

        if (!userRepository.existsByEmail(userEmail)) {
            // Create a test user

            val user = User(
                id = 1L,
                email = userEmail,
                name = userName,
                password = passwordEncoder.encode(userPassword),
                role = Role.USER
            )

            val group = HabitGroup(
                id = 1L,
                name = "Test Group",
                user = user
            )

            val habit = Habit(
                id = 1L,
                name = habitName,
                description = habitDescription,
                frequency = Frequency.DAILY,
                user = user,
                group = group
            )
            val habit2 = Habit(
                id = 2L,
                name = "Workout",
                description = "gotta work out",
                frequency = Frequency.DAILY,
                user = user
            )
            val completion = HabitCompletion(
                id = 1L,
                habit = habit,
                completionDate = LocalDate.now().minusDays(3)
            )
            userRepository.save(user)
            groupRepository.save(group)
            habitRepository.save(habit)
            habitRepository.save(habit2)
            habitCompletionRepository.save(completion)
            println("Test user seeded: ${user.email}")
        } else {
            println("Test user already exists")
        }
    }

    companion object {
        val userEmail = "test.user@test.com"
        val userName = "Test User"
        val userPassword = "testPassword"
        val habitName = "Test Habit"
        val habitDescription = "Test habit description"
    }
}