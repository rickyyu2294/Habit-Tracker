package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.BaseTest
import com.ricky.yu.HabitTracker.controllers.HabitController
import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.repositories.HabitGroupRepository
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import com.ricky.yu.HabitTracker.repositories.UserRepository
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class HabitServiceTest: BaseTest() {
    private val habitRepository: HabitRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val habitGroupRepository: HabitGroupRepository = mockk()
    private val habitService: HabitService = HabitService(habitRepository, userRepository, habitGroupRepository)

    @Test
    fun `should create a habit successfully`() {
        every { habitRepository.save(any()) } returns testHabit
        // Mock the SecurityContextHolder
        mockkStatic(SecurityContextHolder::class)
        val mockAuthentication = mockk<Authentication> {
            every { name } returns testUser.name
        }
        val mockSecurityContext = mockk<SecurityContext> {
            every { authentication } returns mockAuthentication
        }

        every { userRepository.findByEmail(any()) } returns testUser

        every { SecurityContextHolder.getContext() } returns mockSecurityContext
        val create = HabitController.CreateHabitRequest(
            testHabit.name,
            testHabit.description,
            testHabit.frequency.toString()
        )
        val createdHabit = habitService.createHabit(create)

        assertEquals(testHabit.name, createdHabit.name)
        assertEquals(testHabit.user.id, testUser.id)

        verify { habitRepository.save(any()) }
        verify { SecurityContextHolder.getContext() }
        verify { userRepository.findByEmail(any()) }
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

        val update = HabitController.CreateHabitRequest(
            name = updatedHabit.name,
            description = updatedHabit.description,
            frequency = updatedHabit.frequency.toString()
        )
        val result = habitService.updateHabit(id = testHabit.id, update)

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

    @Test
    fun `should get habit by user and group`() {
        every { habitGroupRepository.findById(any()) } returns Optional.of(testGroup)
        every { habitRepository.findByUserIdAndGroupId(any(), any()) } returns listOf(testHabit)

        val result = habitService.getAllHabitsForUserForGroup(testUser.id, testGroup.id)

        assertEquals(1, result.size)
        assertEquals(testHabit, result[0])

        verify { habitGroupRepository.findById(testGroup.id) }
        verify { habitRepository.findByUserIdAndGroupId(testUser.id, testGroup.id) }
    }
}