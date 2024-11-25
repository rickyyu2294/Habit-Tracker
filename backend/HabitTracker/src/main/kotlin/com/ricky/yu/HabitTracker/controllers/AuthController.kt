package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.dtos.AuthenticationResponse
import com.ricky.yu.HabitTracker.dtos.LoginRequest
import com.ricky.yu.HabitTracker.dtos.RefreshTokenRequest
import com.ricky.yu.HabitTracker.dtos.RegisterRequest
import com.ricky.yu.HabitTracker.services.AuthService
import com.ricky.yu.HabitTracker.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    val authenticationService: AuthService,
    val userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any?> {
        try {
            // Authenticate the user
            val authentication = authenticationService.authentication(loginRequest)

            // If authentication succeeds, return a success response (you could also return a token here)
            return ResponseEntity.ok(authentication)
        } catch (ex: Exception) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<String> {
        try {
            val (email, password, name) = registerRequest
            userService.registerUser(email, password, name)
            return ResponseEntity.ok("Registration successful")
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
        }
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<Any?> {
        try {
            return ResponseEntity.ok(authenticationService.refreshAccessToken(request.token))
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
        }
    }
}