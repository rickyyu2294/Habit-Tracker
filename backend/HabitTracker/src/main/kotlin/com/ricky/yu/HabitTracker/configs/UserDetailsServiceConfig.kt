package com.ricky.yu.HabitTracker.configs

import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
class UserDetailsServiceConfig {
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return UserDetailsService { username ->
            val user = userRepository.findByEmail(username)
                ?: throw UsernameNotFoundException("User not found with email: $username")

            // Return a UserDetails instance for authentication
            org.springframework.security.core.userdetails.User(
                user.email,
                user.password, // Ensure this is a hashed password
                listOf() // Add authorities/roles if applicable
            )
        }
    }
}