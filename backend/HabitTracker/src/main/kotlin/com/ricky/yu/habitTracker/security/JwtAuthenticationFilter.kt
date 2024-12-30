package com.ricky.yu.habitTracker.security

import com.ricky.yu.habitTracker.enums.JwtTokenType
import com.ricky.yu.habitTracker.models.User
import com.ricky.yu.habitTracker.services.JwtTokenService
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
    private val tokenService: JwtTokenService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorizationHeader: String? = request.getHeader("Authorization")

        if (authorizationHeader.isNullOrBlank() || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token: String = authorizationHeader.substringAfter("Bearer ")
        // Validate token type
        val tokenType = JwtTokenType.valueOf(tokenService.extractClaim(token, "type"))
        if (tokenType != JwtTokenType.ACCESS) {
            throw AuthenticationServiceException("Invalid access token")
        }

        val email: String = tokenService.extractEmail(token)
        // skip if authentication is already set
        if (SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response)
            return
        }

        val user: User = userDetailsService.loadUserByUsername(email) as User

        // Set authentication if email address
        if (email == user.email) {
            val authToken =
                UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.authorities,
                )
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authToken
        }

        filterChain.doFilter(request, response)
    }
}
