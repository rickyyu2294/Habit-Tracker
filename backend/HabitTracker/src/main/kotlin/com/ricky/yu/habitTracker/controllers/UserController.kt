package com.ricky.yu.habitTracker.controllers

import com.ricky.yu.habitTracker.models.User
import com.ricky.yu.habitTracker.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
) {
    // DTOs
    data class UserResponse(
        val id: Long,
        val email: String,
        val name: String,
    )

    fun User.toResponse(): UserResponse =
        UserResponse(
            id = this.id,
            email = this.email,
            name = this.name,
        )

    // APIs
    @GetMapping
    fun getUser(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.getUsers().map { it.toResponse() })
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable
        id: Long,
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.getUserById(id).toResponse())
    }
}
