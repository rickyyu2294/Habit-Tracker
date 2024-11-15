package com.ricky.yu.HabitTracker.models

import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertContains

@SpringBootTest
class HabitTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var habitRepository: HabitRepository

    @Test
    fun `should save and retrieve habits for user`() {
        val user = userRepository.save(User(email = "test@test.com", name = "test", password = "password"))

        val habits = listOf(
            Habit(name = "Eat Breakfast", description = "Gotta Eat Breakfast", frequency = Frequency.DAILY, user = user),
            Habit(name = "Brush Teeth", description = "Gotta Brush Teeth", frequency = Frequency.WEEKLY, user = user),
            Habit(name = "Practice Guitar", description = "Gotta Practice Guitar", frequency = Frequency.MONTHLY, user = user)
        )
        habitRepository.saveAll(habits)

        val savedHabits = habitRepository.findByUserId(user.id)

        assertEquals(habits.size, savedHabits.size)
        savedHabits.forEach { savedHabit ->
            assertContains(habits.map { it.name }, savedHabit.name)
        }
    }
}