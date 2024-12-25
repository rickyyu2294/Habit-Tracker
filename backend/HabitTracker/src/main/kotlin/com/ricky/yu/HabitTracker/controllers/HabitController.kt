package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.repositories.UserRepository
import com.ricky.yu.HabitTracker.services.HabitService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/habits")
class HabitController(
    private val habitService: HabitService,
    private val userRepository: UserRepository
) {
    data class CreateHabitRequest(val name: String, val description: String, val frequency: String, val groupId: Long? = null)
    @PostMapping
    fun createHabit(@RequestBody input: CreateHabitRequest): ResponseEntity<Habit> {
        val newHabit = habitService.createHabit(input)
        return ResponseEntity.created(URI.create("/habits/${newHabit.id}")).body(newHabit)
    }

    @GetMapping
    fun getAllHabits(): ResponseEntity<List<Habit>> {
        val user = userRepository.findByEmail(SecurityContextHolder.getContext().authentication.name)
            ?: throw NoSuchElementException("No user")
        val habits = habitService.getAllHabitsForUser(user.id)
        return ResponseEntity.ok(habits)
    }

    @GetMapping("/{id}")
    fun getHabitById(@PathVariable id: Long): ResponseEntity<Habit> {
        val habit = habitService.getHabitById(id)
        return ResponseEntity.ok(habit)
    }

    @PutMapping("/{id}")
    fun updateHabit(@PathVariable id: Long, @RequestBody updateRequest: CreateHabitRequest): ResponseEntity<Habit> {
        val updatedHabit = habitService.updateHabit(id, updateRequest)
        return ResponseEntity.ok(updatedHabit)
    }

    @DeleteMapping("/{id}")
    fun deleteHabit(@PathVariable id: Long): ResponseEntity<Unit> {
        habitService.deleteHabit(id)
        return ResponseEntity.ok().body(null)
    }
}