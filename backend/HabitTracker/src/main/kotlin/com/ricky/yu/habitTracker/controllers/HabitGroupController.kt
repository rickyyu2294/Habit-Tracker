package com.ricky.yu.habitTracker.controllers

import com.ricky.yu.habitTracker.models.HabitGroup
import com.ricky.yu.habitTracker.services.HabitGroupService
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
@RequestMapping("/habitGroups")
class HabitGroupController(
    private val habitGroupService: HabitGroupService,
) {
    // DTOs
    data class CreateGroupRequest(
        val name: String,
    )

    data class HabitGroupResponse(
        val id: Long,
        val name: String,
        val userId: Long,
        val habitGroupIds: List<Long>,
    )

    data class HabitGroupOrderingRequest(
        val habitIds: List<Long>,
    )

    fun HabitGroup.toResponse(): HabitGroupResponse {
        return HabitGroupResponse(
            id = this.id,
            name = this.name,
            userId = this.user.id,
            habitGroupIds = habitGroupService.getHabitIdsForGroup(id),
        )
    }

    // APIs

    @PostMapping
    fun createGroup(
        @RequestBody createGroupRequest: CreateGroupRequest,
    ): ResponseEntity<HabitGroupResponse> {
        val newGroup = habitGroupService.createGroup(createGroupRequest.name)
        return ResponseEntity.created(URI.create("/habitGroup/${newGroup.id}"))
            .body(newGroup.toResponse())
    }

    @GetMapping
    fun getAllGroups(): ResponseEntity<List<HabitGroupResponse>> {
        return ResponseEntity.ok(habitGroupService.getGroups().map { it.toResponse() })
    }

    @GetMapping("/{groupId}")
    fun getGroupById(
        @PathVariable groupId: Long,
    ): ResponseEntity<HabitGroupResponse> {
        return ResponseEntity.ok(habitGroupService.getGroupById(groupId).toResponse())
    }

    @DeleteMapping("/{groupId}")
    fun deleteGroup(
        @PathVariable groupId: Long,
    ): ResponseEntity<Unit> {
        habitGroupService.deleteGroup(groupId)
        return ResponseEntity.ok().body(null)
    }

    @PatchMapping("/{groupId}/order")
    fun updateGroupOrdering(
        @PathVariable groupId: Long,
        @RequestBody requestBody: HabitGroupOrderingRequest,
    ): ResponseEntity<HabitGroupResponse> {
        habitGroupService.updateGroupOrdering(groupId, requestBody.habitIds)
        return ResponseEntity.ok(habitGroupService.getGroupById(groupId).toResponse())
    }
}
