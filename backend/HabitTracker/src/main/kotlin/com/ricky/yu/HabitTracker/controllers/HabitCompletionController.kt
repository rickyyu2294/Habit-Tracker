package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.HabitCompletion
import com.ricky.yu.HabitTracker.services.HabitCompletionService
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
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

    @PostMapping
    fun markComplete(
        @PathVariable id: Long,
        @RequestBody completionRequest: CompletionRequest
    ): ResponseEntity<HabitCompletion> {
        val (completion, isNewlyCreated) = habitCompletionService.markOrRetrieveCompletion(id, completionRequest.date)
        return if (isNewlyCreated) {
            ResponseEntity.created(URI.create("/habits/$id/completions/${completionRequest.date}")).body(completion)
        } else {
            ResponseEntity.ok(completion)
        }
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
    ): ResponseEntity<List<HabitCompletion>> {
        val completions = habitCompletionService.getCompletionHistory(id)
        return ResponseEntity.ok(completions)
    }
}