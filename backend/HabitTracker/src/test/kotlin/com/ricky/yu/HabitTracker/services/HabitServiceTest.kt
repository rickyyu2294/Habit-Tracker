package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.BaseTest
import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class HabitServiceTest: BaseTest() {
    private val habitRepository: HabitRepository = mockk()
    private val habitService: HabitService = HabitService(habitRepository)

    @Test
    fun `should create a habit successfully`() {
        every { habitRepository.save(any()) } returns testHabit

        val createdHabit = habitService.createHabit(testHabit)

        assertEquals(testHabit.name, createdHabit.name)
        assertEquals(testHabit.user.id, testUser.id)

        verify { habitRepository.save(testHabit) }
    }

    @Test
    fun `should get all habits for a user`() {
        val habits = listOf(
            testHabit.copy(name = "Exercise"),
            testHabit.copy(name = "Meditate")
        )

        every { habitRepository.findByUserId(testUser.id) } returns habits

        val result = habitService.getAllHabitsForUser(testUser.id)

        assertEquals(2, result.size)
        assertEquals("Exercise", result[0].name)
        assertEquals("Meditate", result[1].name)

        verify { habitRepository.findByUserId(testUser.id) }
    }

    @Test
    fun `should get a habit by id`() {
        every { habitRepository.findById(testHabit.id) } returns Optional.of(testHabit)

        val result = habitService.getHabitById(testHabit.id)

        assertEquals(result.name, testHabit.name)

        verify { habitRepository.findById(testHabit.id) }
    }

    @Test
    fun `should update a habit`() {
        val updatedHabit = testHabit.copy(name = "Updated Habit", frequency = Frequency.DAILY)
        every { habitRepository.findById(testHabit.id) } returns Optional.of(testHabit)
        every { habitRepository.save(any()) } returns updatedHabit

        val result = habitService.updateHabit(id = testHabit.id, updatedHabit)

        assertEquals("Updated Habit", result.name)
        assertEquals(Frequency.DAILY, result.frequency)

        verify { habitRepository.findById(testHabit.id) }
        verify { habitRepository.save(any()) }
    }

    @Test
    fun `should delete a habit`() {
        every { habitRepository.deleteById(testHabit.id) } returns Unit

        habitService.deleteHabit(testHabit.id)

        verify { habitRepository.deleteById(testHabit.id) }
    }
}