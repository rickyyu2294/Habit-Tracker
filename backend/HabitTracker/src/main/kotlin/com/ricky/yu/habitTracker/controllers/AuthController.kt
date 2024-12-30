package com.ricky.yu.habitTracker.controllers

import com.ricky.yu.habitTracker.services.AuthService
import com.ricky.yu.habitTracker.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    val authenticationService: AuthService,
    val userService: UserService,
) {
    // DTOs
    data class RegisterRequest(val email: String, val password: String, val name: String)

    data class LoginRequest(val email: String, val password: String)

    data class AuthenticationResponse(val accessToken: String, val refreshToken: String)

    data class TokenRequest(val accessToken: String, val refreshToken: String)

    // APIs
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
    ): ResponseEntity<Any?> {
        try {
            // Authenticate the user
            val authentication = authenticationService.authentication(loginRequest)

            // If authentication succeeds, return a success response (you could also return a token here)
            return ResponseEntity.ok(authentication)
        } catch (ex: BadCredentialsException) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
        }
    }

    @PostMapping("/logout-app")
    fun logout(
        @RequestBody request: TokenRequest,
    ): ResponseEntity<String> {
        return if (
            authenticationService.invalidateRefreshToken(request)
        ) {
            ResponseEntity.ok("Logout Successful")
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token")
        }
    }

    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest,
    ): ResponseEntity<String> {
        val (email, password, name) = registerRequest
        userService.registerUser(email, password, name)
        return ResponseEntity.ok("Registration successful")
    }

    data class RefreshTokenRequest(val token: String)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest,
    ): ResponseEntity<Any?> {
        println("Received refresh token: ${request.token}")
        return ResponseEntity.ok(authenticationService.refreshAccessToken(request.token))
    }
}
