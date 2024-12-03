package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.services.HabitCompletionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/habits/{id}/completions")
class HabitCompletionController(
    private val habitCompletionService: HabitCompletionService
) {
    data class CompletionRequest(val date: LocalDate)

    @PostMapping
    fun markComplete(
        @PathVariable id: Long,
        @RequestBody completionRequest: CompletionRequest
    ): ResponseEntity<HabitCompletion> {
        val completion = habitCompletionService.markComplete(id, completionRequest.date)
        return ResponseEntity.status(HttpStatus.CREATED).body(completion)
    }

    @GetMapping
    fun getCompletionHistory(
        @PathVariable id: Long
    ): ResponseEntity<List<HabitCompletion>> {
        val completions = habitCompletionService.getCompletionHistory(id)
        return ResponseEntity.ok(completions)
    }
}