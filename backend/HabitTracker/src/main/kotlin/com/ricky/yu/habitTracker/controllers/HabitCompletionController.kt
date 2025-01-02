package com.ricky.yu.habitTracker.controllers

import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.models.HabitCompletion
import com.ricky.yu.habitTracker.services.HabitCompletionService
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

    data class GroupedIntervalResponse(
        val interval: String,
        val completions: List<CompletionResponse>
    )

    data class GroupedCompletionsResponse(
        val intervalType: String,
        val groupedIntervalResponses: List<GroupedIntervalResponse>,
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

    @PostMapping("/intervals/{interval}")
    fun createCompletionInInterval(
        @PathVariable habitId: Long,
        @PathVariable interval: String,
    ): ResponseEntity<CompletionResponse> {
        val completion = habitCompletionService.createCompletionInInterval(habitId, interval)
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

    @DeleteMapping("/intervals/{interval}/latest")
    fun deleteLatestCompletionInInterval(
        @PathVariable habitId: Long,
        @PathVariable interval: String,
    ): ResponseEntity<Void> {
        habitCompletionService.decrementCompletion(habitId, interval)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/bulkDelete")
    fun bulkDelete(
        @PathVariable habitId: Long,
        @RequestBody ids: List<Long>,
    ): ResponseEntity<Unit> {
        habitCompletionService.deleteCompletions(habitId, ids)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getCompletions(
        @PathVariable habitId: Long,
        @RequestParam(required = false) intervalType: String?,
    ): ResponseEntity<GroupedCompletionsResponse> {
        return if (!intervalType.isNullOrBlank()) {
            val groupedCompletions =
                habitCompletionService.getCompletionsGroupedByInterval(
                    habitId,
                    IntervalType.valueOf(intervalType.uppercase()),
                ).map { (interval, completions) ->
                    GroupedIntervalResponse(interval, completions.map { it.toResponse() })
                }

            ResponseEntity.ok(
                GroupedCompletionsResponse(
                    intervalType = intervalType,
                    groupedIntervalResponses = groupedCompletions,
                ),
            )
        } else {
            val completions = habitCompletionService.getCompletions(habitId)
            ResponseEntity.ok(
                GroupedCompletionsResponse(
                    intervalType = "all",
                    groupedIntervalResponses = listOf(
                        GroupedIntervalResponse("all", completions.map {
                            it.toResponse()
                        })
                    )
                )
            )
        }
    }
}
