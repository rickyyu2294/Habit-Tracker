package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.services.HabitCompletionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.time.LocalDate

@RestController
@RequestMapping("/habits/{id}/completions")
class HabitCompletionController(
    private val habitCompletionService: HabitCompletionService
) {
    data class CompletionRequest(val date: LocalDate)

    data class CompletionResponse(
        val id: Long,
        val habitId: Long,
        val completionDate: LocalDate
    )

    fun HabitCompletion.toResponse(): CompletionResponse {
        return CompletionResponse(
            id = this.id,
            habitId = this.habit.id,
            completionDate = this.completionDate
        )
    }

    @PostMapping
    fun markComplete(
        @PathVariable id: Long,
        @RequestBody completionRequest: CompletionRequest
    ): ResponseEntity<CompletionResponse> {
        val completion = habitCompletionService.markCompletion(id, completionRequest.date)
        return ResponseEntity.created(
            URI.create("/habits/${id}/completions/${completionRequest.date}")
        ).body(completion.toResponse())
    }

    @DeleteMapping("/{date}")
    fun deleteCompletion(
        @PathVariable id: Long,
        @PathVariable date: LocalDate
    ): ResponseEntity<Any> {
        habitCompletionService.deleteCompletion(id, date)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getCompletionHistory(
        @PathVariable id: Long
    ): ResponseEntity<List<CompletionResponse>> {
        val completions = habitCompletionService.getCompletionHistory(id)
        return ResponseEntity.ok(completions.map { it.toResponse() })
    }
}