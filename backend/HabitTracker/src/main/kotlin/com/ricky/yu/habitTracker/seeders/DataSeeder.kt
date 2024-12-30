package com.ricky.yu.habitTracker.seeders

import com.ricky.yu.habitTracker.enums.Interval
import com.ricky.yu.habitTracker.enums.Role
import com.ricky.yu.habitTracker.models.Habit
import com.ricky.yu.habitTracker.models.HabitCompletion
import com.ricky.yu.habitTracker.models.HabitGroup
import com.ricky.yu.habitTracker.models.User
import com.ricky.yu.habitTracker.repositories.HabitCompletionRepository
import com.ricky.yu.habitTracker.repositories.HabitGroupRepository
import com.ricky.yu.habitTracker.repositories.HabitRepository
import com.ricky.yu.habitTracker.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@Profile("dev", "test")
@Suppress("MagicNumber")
class DataSeeder(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository,
    private val habitCompletionRepository: HabitCompletionRepository,
    private val groupRepository: HabitGroupRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {
    @Suppress("LongMethod")
    override fun run(vararg args: String?) {
        // Check if the test user already exists

        if (!userRepository.existsByEmail(userEmail)) {
            // Create a test user

            val user =
                User(
                    id = 1L,
                    email = userEmail,
                    name = userName,
                    password = passwordEncoder.encode(userPassword),
                    role = Role.USER,
                )

            val group =
                HabitGroup(
                    id = 1L,
                    name = "Test Group",
                    user = user,
                )

            val habit =
                Habit(
                    id = 1L,
                    name = habitName,
                    description = habitDescription,
                    interval = Interval.WEEKLY,
                    user = user,
                    group = group,
                )
            val habit2 =
                Habit(
                    id = 2L,
                    name = "Meditate",
                    description = "gotta meditate",
                    interval = Interval.DAILY,
                    user = user,
                )

            val habit3 =
                Habit(
                    id = 3L,
                    name = "Pay Bills",
                    description = "gotta pay bills",
                    interval = Interval.MONTHLY,
                    user = user,
                )

            val completion1 =
                HabitCompletion(
                    habit = habit,
                    completionDateTime = LocalDateTime.now().minusWeeks(3),
                )

            val completion3 =
                HabitCompletion(
                    habit = habit,
                    completionDateTime = LocalDateTime.now().minusWeeks(1),
                )

            val completion4 =
                HabitCompletion(
                    habit = habit,
                    completionDateTime = LocalDateTime.now().minusWeeks(1).minusDays(1),
                )

            val completion2 =
                HabitCompletion(
                    habit = habit2,
                    completionDateTime = LocalDateTime.now().minusDays(3),
                )

            val completion5 =
                HabitCompletion(
                    habit = habit3,
                    completionDateTime = LocalDateTime.now().minusMonths(3),
                )
            userRepository.save(user)
            groupRepository.save(group)
            habitRepository.save(habit)
            habitRepository.save(habit2)
            habitRepository.save(habit3)
            habitCompletionRepository.save(completion1)
            habitCompletionRepository.save(completion2)
            habitCompletionRepository.save(completion3)
            habitCompletionRepository.save(completion4)
            habitCompletionRepository.save(completion5)
            println("Test user seeded: ${user.email}")
        } else {
            println("Test user already exists")
        }
    }

    companion object {
        val userEmail = "test.user@test.com"
        val userName = "Test User"
        val userPassword = "testPassword"
        val habitName = "Climb"
        val habitDescription = "gotta climb"
    }
}
