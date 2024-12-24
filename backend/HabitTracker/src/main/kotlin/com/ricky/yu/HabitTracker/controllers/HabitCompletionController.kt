package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.services.HabitCompletionService
import org.apache.coyote.Response
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
        val completions = habitCompletionService.getCompletionHistory(id)
        val completion = completions.find { it.completionDate == completionRequest.date }
        return if (completion == null) {
            val newCompletion = habitCompletionService.markComplete(id, completionRequest.date)
            ResponseEntity.status(HttpStatus.CREATED).body(newCompletion)
        } else {
            ResponseEntity.status(HttpStatus.OK).body(completion)
        }
    }

    @DeleteMapping
    fun deleteCompletion(
        @PathVariable id: Long,
        @RequestBody completionRequest: CompletionRequest
    ): ResponseEntity<Any> {
        return try {
            habitCompletionService.deleteCompletion(id, completionRequest.date)
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }

    @GetMapping
    fun getCompletionHistory(
        @PathVariable id: Long
    ): ResponseEntity<List<HabitCompletion>> {
        val completions = habitCompletionService.getCompletionHistory(id)
        return ResponseEntity.ok(completions)
    }
}