package com.ricky.yu.habitTracker.configs

import com.ricky.yu.habitTracker.context.RequestCtxFilter
import com.ricky.yu.habitTracker.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val requestContextFilter: RequestCtxFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors {}
            csrf { disable() } // Disable CSRF protection for simplicity
            sessionManagement { SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                authorize("/login", permitAll)
                authorize("/register", permitAll)
                authorize("/refresh", permitAll)
                authorize(anyRequest, authenticated) // Require authentication for all other endpoints
            }
            httpBasic { } // Enable HTTP Basic auth
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
            addFilterBefore<UsernamePasswordAuthenticationFilter>(requestContextFilter)
        }

        return http.build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder,
    ): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)

        return ProviderManager(authenticationProvider)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
