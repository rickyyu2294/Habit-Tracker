package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    val userRepository: UserRepository
) {
    @GetMapping("/users")
    fun getUser(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userRepository.findAll())
    }
}