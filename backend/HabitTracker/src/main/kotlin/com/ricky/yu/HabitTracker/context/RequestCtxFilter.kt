package com.ricky.yu.HabitTracker.context

import com.ricky.yu.HabitTracker.models.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class RequestCtxFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authentication = SecurityContextHolder.getContext().authentication

            if (authentication != null && authentication.isAuthenticated && authentication.principal is User) {
                val user = authentication.principal as User
                RequestCtxHolder.setRequestContext(
                    RequestCtx(
                        userId = user.id,
                        role = user.role,
                        requestId = request.requestId
                    )
                )
            }

            filterChain.doFilter(request, response)
        } finally {
            RequestCtxHolder.clearRequestContext()
        }
    }
}