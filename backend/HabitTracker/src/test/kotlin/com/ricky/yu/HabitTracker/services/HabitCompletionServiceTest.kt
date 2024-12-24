package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.BaseTest
import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.repositories.HabitCompletionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class HabitCompletionServiceTest : BaseTest() {
    private val habitCompletionRepository: HabitCompletionRepository = mockk()
    private val habitService: HabitService = mockk()
    private val habitCompletionService = HabitCompletionService(habitService, habitCompletionRepository)

    @Test
    fun `should mark a habit completion successfully`() {
        val completionDate = LocalDate.now()
        val completion = HabitCompletion(id = 1L, habit = testHabit, completionDate = completionDate)

        every { habitService.getHabitById(testHabit.id) } returns testHabit
        every { habitCompletionRepository.save(any()) } returns completion

        val (result, isNewlyCreated) = habitCompletionService.markOrRetrieveCompletion(testUser.id, LocalDate.now())

        assertEquals(completion.id, result.id)
        assertEquals(completionDate, result.completionDate)
        assertEquals(true, isNewlyCreated)

        verify { habitCompletionRepository.save(any()) }
    }

    @Test
    fun `should throw exception when marking completion for a non-existent habit`() {
        every { habitService.getHabitById(any()) } throws NoSuchElementException("Habit not found")
        every { habitCompletionRepository.findByHabitIdAndCompletionDate(any(), any()) } returns null

        assertThrows<NoSuchElementException> {
            habitCompletionService.markOrRetrieveCompletion(999L, LocalDate.now())
        }
        verify { habitService.getHabitById(999L) }
    }

    @Test
    fun `should get completion history successfully`() {
        val completionDate = LocalDate.now()

        val completions = listOf(
            HabitCompletion(
                id = 1L,
                habit = testHabit,
                completionDate = completionDate
            ),
            HabitCompletion(
                id = 2L,
                habit = testHabit,
                completionDate = completionDate.minusWeeks(2)
            )
        ).sortedByDescending { it.completionDate }

        every { habitCompletionRepository.findByHabitId(testHabit.id) } returns completions

        val result = habitCompletionService.getCompletionHistory(testHabit.id)

        assertEquals(2, result.size)
        assertEquals(completionDate, result[0].completionDate)
        assertEquals(completionDate.minusWeeks(2), result[1].completionDate)
        assertEquals(testHabit.name, result[0].habit.name)

        verify { habitCompletionRepository.findByHabitId(testHabit.id) }
    }

    @Test
    fun `should return empty list when there are no completions of habit`() {
        every { habitCompletionRepository.findByHabitId(testHabit.id) } returns listOf()

        val result = habitCompletionService.getCompletionHistory(testHabit.id)

        assertTrue { result.isEmpty() }

        verify { habitCompletionRepository.findByHabitId(any()) }
    }
}