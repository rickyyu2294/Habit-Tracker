package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.BaseTest
import com.ricky.yu.HabitTracker.context.RequestCtx
import com.ricky.yu.HabitTracker.context.RequestCtxHolder
import com.ricky.yu.HabitTracker.controllers.HabitController
import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.enums.Role
import com.ricky.yu.HabitTracker.repositories.HabitGroupRepository
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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
    private val userService: UserService = mockk()
    private val habitGroupRepository: HabitGroupRepository = mockk()
    private val habitService: HabitService = HabitService(habitRepository, habitGroupRepository, userService)

    @BeforeEach
    fun setup() {
        // Mock the RequestCtxHolder
        mockkObject(RequestCtxHolder)
    }

    @AfterEach
    fun teardown() {
        // Unmock after each test to avoid test pollution
        unmockkObject(RequestCtxHolder)
    }

    @Test
    fun `should create a habit successfully`() {
        every { RequestCtxHolder.getRequestContext() } returns RequestCtx(
            userId = 998L,
            role = Role.USER,
            requestId = "test-request-id"
        )
        every { habitRepository.save(any()) } returns testHabit
        // Mock the SecurityContextHolder
        mockkStatic(SecurityContextHolder::class)
        val mockAuthentication = mockk<Authentication> {
            every { name } returns testUser.name
        }
        val mockSecurityContext = mockk<SecurityContext> {
            every { authentication } returns mockAuthentication
        }

        every { userService.getUserById(any()) } returns testUser

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
        verify { userService.getUserById(any()) }
    }

    @Test
    fun `should get all habits for a user`() {
        every { RequestCtxHolder.getRequestContext() } returns RequestCtx(
            userId = 998L,
            role = Role.USER,
            requestId = "test-request-id"
        )
        val habits = listOf(
            testHabit.copy(name = "Exercise"),
            testHabit.copy(name = "Meditate")
        )

        every { habitRepository.findByUserId(testUser.id) } returns habits

        val result = habitService.getHabitsForCurrentUser()

        assertEquals(2, result.size)
        assertEquals("Exercise", result[0].name)
        assertEquals("Meditate", result[1].name)

        verify { habitRepository.findByUserId(testUser.id) }
    }

    @Test
    fun `should get a habit by id`() {
        every { habitRepository.findByIdAndUserId(any(), any()) } returns Optional.of(testHabit)
        every { RequestCtxHolder.getRequestContext() } returns requestCtx

        val result = habitService.getHabitById(testHabit.id)

        assertEquals(result.name, testHabit.name)

        verify { habitRepository.findByIdAndUserId(any(), any()) }
    }

    @Test
    fun `should update a habit`() {
        val updatedHabit = testHabit.copy(name = "Updated Habit", frequency = Frequency.DAILY)
        every { habitRepository.findByIdAndUserId(any(), any()) } returns Optional.of(testHabit)
        every { habitRepository.save(any()) } returns updatedHabit
        every { RequestCtxHolder.getRequestContext() } returns requestCtx

        val update = HabitController.CreateHabitRequest(
            name = updatedHabit.name,
            description = updatedHabit.description,
            frequency = updatedHabit.frequency.toString()
        )
        val result = habitService.updateHabit(id = testHabit.id, update)

        assertEquals("Updated Habit", result.name)
        assertEquals(Frequency.DAILY, result.frequency)

        verify { habitRepository.findByIdAndUserId(any(), any()) }
        verify { habitRepository.save(any()) }
    }

    @Test
    fun `should delete a habit`() {
        every { habitRepository.delete(testHabit) } returns Unit
        every { habitService.getHabitById(any()) } returns testHabit
        every { habitRepository.findByIdAndUserId(any(), any()) } returns Optional.of(testHabit)
        every { RequestCtxHolder.getRequestContext() } returns requestCtx

        habitService.deleteHabit(testHabit.id)

        verify { habitRepository.delete(testHabit) }
    }

    @Test
    fun `should get habit by user and group`() {
        every { habitGroupRepository.findById(any()) } returns Optional.of(testGroup)
        every { habitRepository.findByUserIdAndGroupId(any(), any()) } returns listOf(testHabit)
        every { RequestCtxHolder.getRequestContext() } returns requestCtx

        val result = habitService.getHabitsForCurrentUserAndGroup(testGroup.id)

        assertEquals(1, result.size)
        assertEquals(testHabit, result[0])

        verify { habitGroupRepository.findById(testGroup.id) }
        verify { habitRepository.findByUserIdAndGroupId(testUser.id, testGroup.id) }
    }

    companion object {
        val requestCtx = RequestCtx(
            userId = 998L,
            role = Role.USER,
            requestId = "test-request-id"
        )
    }
}