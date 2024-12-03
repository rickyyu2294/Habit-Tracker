package com.ricky.yu.HabitTracker.dtos

import java.time.LocalDate

data class RegisterRequest(val email: String, val password: String, val name: String)

data class LoginRequest(val email: String, val password: String)

data class AuthenticationResponse(val accessToken: String, val refreshToken: String)

data class TokenRequest(val accessToken: String, val refreshToken: String)
