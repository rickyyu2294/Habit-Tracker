package com.ricky.yu.HabitTracker.controllers

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
    val authenticationManager: AuthenticationManager,
    val userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<String> {
        try {
            // Create the authentication request
            val authenticationRequest = UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )

            // Authenticate the user
            val authentication = authenticationManager.authenticate(authenticationRequest)

            // If authentication succeeds, return a success response (you could also return a token here)
            return ResponseEntity.ok("Login successful")
        } catch (ex: Exception) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password")
        }
    }

    data class LoginRequest(val email: String, val password: String)

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

    data class RegisterRequest(val email: String, val password: String, val name: String)

}