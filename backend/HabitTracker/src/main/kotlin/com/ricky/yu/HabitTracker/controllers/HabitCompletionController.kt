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
    data class CompletionRequest(val dateTime: LocalDateTime)

    data class CompletionResponse(
        val habitId: Long,
        val completionDateTime: LocalDateTime
    )

    data class GroupedCompletionsResponse(
        val interval: String,
        val completions: Map<String, List<CompletionResponse>>
    )

    fun HabitCompletion.toResponse(): CompletionResponse {
        return CompletionResponse(
            habitId = this.habit.id,
            completionDateTime = this.completionDateTime
        )
    }

    // APIs

    @PostMapping
    fun createCompletion(
        @PathVariable id: Long,
        @RequestBody completionRequest: CompletionRequest
    ): ResponseEntity<CompletionResponse> {
        val completion = habitCompletionService.createCompletion(id, completionRequest.dateTime)
        return ResponseEntity.created(
            URI.create("/habits/${id}/completions/${completionRequest.dateTime}")
        ).body(completion.toResponse())
    }

    @DeleteMapping("/{dateTime}")
    fun deleteCompletion(
        @PathVariable id: Long,
        @PathVariable dateTime: LocalDateTime
    ): ResponseEntity<Unit> {
        habitCompletionService.deleteCompletion(id, dateTime)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getCompletions(
        @PathVariable id: Long,
        @RequestParam(required = false) interval: String?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
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