package com.ricky.yu.habitTracker.controllers

import com.ricky.yu.habitTracker.enums.Interval
import com.ricky.yu.habitTracker.models.HabitCompletion
import com.ricky.yu.habitTracker.services.HabitCompletionService
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.LocalDateTime

@RestController
@RequestMapping("/habits/{habitId}/completions")
class HabitCompletionController(
    private val habitCompletionService: HabitCompletionService,
) {
    // DTOs
    data class CompletionRequest(val date: LocalDateTime)

    data class CompletionResponse(
        val id: Long,
        val habitId: Long,
        val completionDate: LocalDateTime,
    )

    data class GroupedCompletionsResponse(
        val interval: String,
        val completions: Map<String, List<CompletionResponse>>,
    )

    fun HabitCompletion.toResponse(): CompletionResponse {
        return CompletionResponse(
            id = this.id,
            habitId = this.habit.id,
            completionDate = this.completionDateTime,
        )
    }

    fun HabitCompletion.toURI(): String {
        return "/habits/${this.habit.id}/completions/${this.id}"
    }

    // APIs

    @PostMapping
    fun createCompletion(
        @PathVariable habitId: Long,
        @RequestBody completionRequest: CompletionRequest,
    ): ResponseEntity<CompletionResponse> {
        val completion = habitCompletionService.createCompletion(habitId, completionRequest.date)
        return ResponseEntity.created(
            URI.create(completion.toURI()),
        ).body(completion.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteCompletion(
        @PathVariable habitId: Long,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        habitCompletionService.deleteCompletion(habitId, id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/bulkDelete")
    fun bulkDelete(
        @PathVariable habitId: Long,
        @RequestBody completionResponses: List<CompletionResponse>,
    ): ResponseEntity<Unit> {
        habitCompletionService.deleteCompletions(habitId, completionResponses.map { it.id })
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getCompletions(
        @PathVariable habitId: Long
    ): ResponseEntity<List<CompletionResponse>> {
        val completions = habitCompletionService.getCompletions(habitId)
        return ResponseEntity.ok(completions.map { it.toResponse() })
    }

    @GetMapping("/intervals")
    fun getCompletionsByInterval(
        @PathVariable habitId: Long,
        @RequestParam(required = true) interval: String,
    ): ResponseEntity<GroupedCompletionsResponse> {
        val groupedCompletions =
            habitCompletionService.getCompletionsGroupedByInterval(
                habitId,
                Interval.valueOf(interval.uppercase()),
            ).mapValues { (_, completions) -> completions.map { it.toResponse() } }

        return ResponseEntity.ok(
            GroupedCompletionsResponse(
                interval = interval,
                completions = groupedCompletions,
            ),
        )
    }
}
