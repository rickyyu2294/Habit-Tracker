package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.HabitGroup
import com.ricky.yu.HabitTracker.services.HabitGroupService
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/habitGroup")
class HabitGroupController(
    private val habitGroupService: HabitGroupService
) {
    // DTOs
    data class CreateGroupRequest(
        val name: String
    )

    data class HabitGroupResponse(
        val id: Long,
        val name: String,
        val userId: Long
    )

    fun HabitGroup.toResponse(): HabitGroupResponse {
        return HabitGroupResponse(
            id = this.id,
            name = this.name,
            userId = this.user.id
        )
    }

    // APIs

    @PostMapping
    fun createGroup(
        @RequestBody createGroupRequest: CreateGroupRequest
    ): ResponseEntity<HabitGroupResponse> {
        val newGroup = habitGroupService.createGroup(createGroupRequest.name)
        return ResponseEntity.created(URI.create("/habitGroup/${newGroup.id}"))
            .body(newGroup.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteGroup(@PathVariable groupId: Long): ResponseEntity<Unit> {
        habitGroupService.deleteGroup(groupId)
        return ResponseEntity.ok().body(null)
    }

    @GetMapping
    fun getAllGroups(): ResponseEntity<List<HabitGroupResponse>> {
        return ResponseEntity.ok(habitGroupService.getGroups().map { it.toResponse() })
    }
}