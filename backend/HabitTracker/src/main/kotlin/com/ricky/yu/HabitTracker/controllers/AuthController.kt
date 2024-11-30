package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.dtos.*
import com.ricky.yu.HabitTracker.repositories.RefreshTokenRepository
import com.ricky.yu.HabitTracker.repositories.UserRepository
import com.ricky.yu.HabitTracker.services.AuthService
import com.ricky.yu.HabitTracker.services.JwtTokenService
import com.ricky.yu.HabitTracker.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    val authenticationService: AuthService,
    val userService: UserService,
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

    @PostMapping("/logout-app")
    fun logout(@RequestBody request: TokenRequest): ResponseEntity<String> {
        return try {
            // get user and delete refresh tokens for that user
            if (
                authenticationService.invalidateRefreshToken(request)
            ) {
                ResponseEntity.ok("Logout Successful")
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token")
            }

        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
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
    fun refreshAccessToken(@RequestBody request: TokenRequest): ResponseEntity<Any?> {
        return try {
            ResponseEntity.ok(authenticationService.refreshAccessToken(request.refreshToken))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
        }
    }
}