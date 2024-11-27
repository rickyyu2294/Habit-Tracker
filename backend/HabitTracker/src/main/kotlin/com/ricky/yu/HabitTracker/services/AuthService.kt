package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.dtos.AuthenticationResponse
import com.ricky.yu.HabitTracker.dtos.LoginRequest
import com.ricky.yu.HabitTracker.enums.JwtTokenType
import com.ricky.yu.HabitTracker.models.RefreshToken
import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.repositories.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val tokenService: JwtTokenService,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.accessTokenExpiration}") private val accessTokenExpiration: Long = 0,
    @Value("\${jwt.refreshTokenExpiration}") private val refreshTokenExpiration: Long = 0
) {
    fun authentication(authenticationRequest: LoginRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.email,
                authenticationRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(authenticationRequest.email) as User

        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)

        refreshTokenRepository.save(RefreshToken(token = refreshToken, user = user))

        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun refreshAccessToken(refreshToken: String): String {
        val username = tokenService.extractUsername(refreshToken)

        return username.let { user ->
            val currentUserDetails = userDetailsService.loadUserByUsername(user) as User
            val refreshTokenUserDetails = refreshTokenRepository.findById(refreshToken)

            if (
                currentUserDetails.email == refreshTokenUserDetails.get().user.email &&
                enumValueOf<JwtTokenType>(
                    tokenService.extractClaim(token = refreshToken, claim = "type")
                ) == JwtTokenType.REFRESH
            )
                createAccessToken(currentUserDetails)
            else
                throw AuthenticationServiceException("Invalid refresh token")
        }
    }

    private fun createToken(user: User, type: JwtTokenType, expirationDuration: Long): String {
        return tokenService.generateToken(
            subject = user.email,
            expiration = Date(System.currentTimeMillis() + expirationDuration),
            additionalClaims = mapOf(
                "type" to type,
                "role" to user.role.name
            )
        )
    }

    private fun createAccessToken(user: User): String {
        return createToken(user, JwtTokenType.ACCESS, accessTokenExpiration)
    }

    private fun createRefreshToken(user: User): String {
        return createToken(user, JwtTokenType.REFRESH, refreshTokenExpiration)
    }
}