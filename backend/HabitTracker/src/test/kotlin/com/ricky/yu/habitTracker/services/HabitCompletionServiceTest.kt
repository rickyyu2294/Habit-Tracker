package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.BaseTest
import com.ricky.yu.habitTracker.models.HabitCompletion
import com.ricky.yu.habitTracker.repositories.HabitCompletionRepository
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
        val completion = HabitCompletion(id = 1L, habit = testHabit, completionDateTime = completionDate)

        every { habitService.getHabitById(testHabit.id) } returns testHabit
        every { habitCompletionRepository.save(any()) } returns completion

        val result = habitCompletionService.createCompletion(testUser.id, LocalDate.now())

        assertEquals(completion.id, result.id)
        assertEquals(completionDate, result.completionDateTime)

        verify { habitCompletionRepository.save(any()) }
    }

    @Test
    fun `should throw exception when marking completion for a non-existent habit`() {
        every { habitService.getHabitById(any()) } throws NoSuchElementException("Habit not found")
        every { habitCompletionRepository.findByHabitIdAndCompletionDateTime(any(), any()) } returns null

        assertThrows<NoSuchElementException> {
            habitCompletionService.createCompletion(999L, LocalDate.now())
        }
        verify { habitService.getHabitById(999L) }
    }

    @Test
    fun `should get completion history successfully`() {
        val completionDate = LocalDate.now()

        val completions =
            listOf(
                HabitCompletion(
                    id = 1L,
                    habit = testHabit,
                    completionDateTime = completionDate,
                ),
                HabitCompletion(
                    id = 2L,
                    habit = testHabit,
                    completionDateTime = completionDate.minusWeeks(2),
                ),
            ).sortedByDescending { it.completionDateTime }

        every { habitCompletionRepository.findByHabitId(testHabit.id) } returns completions

        val result = habitCompletionService.getCompletions(testHabit.id)

        assertEquals(2, result.size)
        assertEquals(completionDate, result[0].completionDateTime)
        assertEquals(completionDate.minusWeeks(2), result[1].completionDateTime)
        assertEquals(testHabit.name, result[0].habit.name)

        verify { habitCompletionRepository.findByHabitId(testHabit.id) }
    }

    @Test
    fun `should return empty list when there are no completions of habit`() {
        every { habitCompletionRepository.findByHabitId(testHabit.id) } returns listOf()
        every { habitService.validateUserOwnsHabit(any()) } returns Unit

        val result = habitCompletionService.getCompletions(testHabit.id)

        assertTrue { result.isEmpty() }

        verify { habitCompletionRepository.findByHabitId(any()) }
    }
}
