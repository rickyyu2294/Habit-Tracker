package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.enums.Interval
import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.services.HabitCompletionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.time.LocalDateTime

@RestController
@RequestMapping("/habits/{id}/completions")
class HabitCompletionController(
    private val habitCompletionService: HabitCompletionService
) {
    // DTOs
    data class CompletionRequest(val date: LocalDateTime)

    data class CompletionResponse(
        val habitId: Long,
        val completionDate: LocalDateTime
    )

    data class GroupedCompletionsResponse(
        val interval: String,
        val completions: Map<String, List<CompletionResponse>>
    )

    fun HabitCompletion.toResponse(): CompletionResponse {
        return CompletionResponse(
            habitId = this.habit.id,
            completionDate = this.completionDate
        )
    }

    // APIs

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
        @PathVariable date: LocalDateTime
    ): ResponseEntity<Unit> {
        habitCompletionService.deleteCompletion(id, date)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getCompletions(
        @PathVariable id: Long,
        @RequestParam(required = false) interval: String?
    ): ResponseEntity<Any> {
        return if (!interval.isNullOrBlank()) {
            val groupedCompletions = habitCompletionService.getCompletionsGroupedByInterval(
                id,
                Interval.valueOf(interval.uppercase())
            ).mapValues { (_, completions) -> completions.map { it.toResponse() } }

            ResponseEntity.ok(
                GroupedCompletionsResponse(
                    interval = interval,
                    completions = groupedCompletions
                )
            )
        } else {
            val completions = habitCompletionService.getCompletions(id)
            ResponseEntity.ok(completions.map { it.toResponse() })
        }
    }
}