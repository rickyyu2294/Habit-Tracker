package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.services.HabitService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/habits")
class HabitController(
    private val habitService: HabitService
) {
    // DTOs
    data class CreateHabitRequest(
        val name: String,
        val description: String,
        val interval: String,
        val frequency: Int,
        val groupId: Long? = null
    )

    data class HabitResponse(
        val id: Long,
        val name: String,
        val description: String,
        val interval: String,
        val frequency: Int,
        val groupId: Long?
    )

    fun Habit.toResponse(): HabitResponse {
        return HabitResponse(
            id = this.id,
            name = this.name,
            description = this.description,
            interval = this.interval.toString(),
            frequency = this.frequency,
            groupId = this.group?.id
        )
    }

    // APIs

    @PostMapping
    fun createHabit(@RequestBody input: CreateHabitRequest): ResponseEntity<HabitResponse> {
        val newHabit = habitService.createHabit(input)
        return ResponseEntity.created(URI.create("/habits/${newHabit.id}")).body(newHabit.toResponse())
    }

    @GetMapping
    fun getAllHabits(): ResponseEntity<List<HabitResponse>> {
        val habits = habitService.getHabitsForCurrentUser()
        return ResponseEntity.ok(habits.map { it.toResponse() })
    }

    @GetMapping("/{id}")
    fun getHabitById(@PathVariable id: Long): ResponseEntity<HabitResponse> {
        val habit = habitService.getHabitById(id)
        return ResponseEntity.ok(habit.toResponse())
    }

    @PutMapping("/{id}")
    fun updateHabit(@PathVariable id: Long, @RequestBody updateRequest: CreateHabitRequest): ResponseEntity<HabitResponse> {
        val updatedHabit = habitService.updateHabit(id, updateRequest)
        return ResponseEntity.ok(updatedHabit.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteHabit(@PathVariable id: Long): ResponseEntity<Unit> {
        habitService.deleteHabit(id)
        return ResponseEntity.ok().body(null)
    }
}