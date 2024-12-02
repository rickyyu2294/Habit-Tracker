package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.Habit
import com.ricky.yu.HabitTracker.services.HabitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/habits")
class HabitController(
    private val habitService: HabitService
) {
    @PostMapping
    fun createHabit(@RequestBody habit: Habit): ResponseEntity<Habit> {
        val newHabit = habitService.createHabit(habit)
        return ResponseEntity.status(HttpStatus.CREATED).body(newHabit)
    }

    @GetMapping
    fun getAllHabits(@RequestParam userId: Long): ResponseEntity<List<Habit>> {
        val habits = habitService.getAllHabitsForUser(userId)
        return ResponseEntity.ok(habits)
    }

    @GetMapping("/{id}")
    fun getHabitById(@PathVariable id: Long): ResponseEntity<Habit> {
        val habit = habitService.getHabitById(id)
        return ResponseEntity.ok(habit)
    }

    @PutMapping("/{id}")
    fun updateHabit(@PathVariable id: Long, @RequestBody updatedHabit: Habit): ResponseEntity<Habit> {
        val habit = habitService.updateHabit(id, updatedHabit)
        return ResponseEntity.ok(habit)
    }

    @DeleteMapping("/{id}")
    fun deleteHabit(@PathVariable id: Long): ResponseEntity<Unit> {
        habitService.deleteHabit(id)
        return ResponseEntity.ok().body(null)
    }
}