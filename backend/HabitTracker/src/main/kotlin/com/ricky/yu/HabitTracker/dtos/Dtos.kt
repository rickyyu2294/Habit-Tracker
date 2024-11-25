package com.ricky.yu.HabitTracker.dtos

data class RegisterRequest(val email: String, val password: String, val name: String)

data class LoginRequest(val email: String, val password: String)

data class AuthenticationResponse(val accessToken: String, val refreshToken: String)

data class RefreshTokenRequest(val token: String)