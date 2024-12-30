package com.ricky.yu.habitTracker.controllers

import com.ricky.yu.habitTracker.models.User
import com.ricky.yu.habitTracker.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    val userRepository: UserRepository,
) {
    @GetMapping
    fun getUser(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userRepository.findAll())
    }
}
