package com.ricky.yu.HabitTracker.security

import com.ricky.yu.HabitTracker.enums.JwtTokenType
import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.services.AuthService
import com.ricky.yu.HabitTracker.services.JwtTokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenService: JwtTokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader: String? = request.getHeader("Authorization")

        if (null != authorizationHeader && authorizationHeader.startsWith("Bearer ")) {
            try {
                val token: String = authorizationHeader.substringAfter("Bearer ")
                val tokenType = JwtTokenType.valueOf(tokenService.extractClaim(token, "type"))
                if (tokenType != JwtTokenType.ACCESS) throw AuthenticationServiceException("Invalid access token")
                val username: String = tokenService.extractUsername(token)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails: User = userDetailsService.loadUserByUsername(username) as User

                    if (username == userDetails.username) {
                        val authToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                }
            } catch (ex: Exception) {
                response.writer.write(
                    """{"error": "Filter Authorization error: 
                    |${ex.message ?: "unknown error"}"}""".trimMargin()
                )
            }
        }

        filterChain.doFilter(request, response)
    }
}