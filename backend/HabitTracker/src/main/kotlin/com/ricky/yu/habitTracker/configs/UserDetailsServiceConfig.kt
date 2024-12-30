package com.ricky.yu.habitTracker.configs

import com.ricky.yu.habitTracker.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
class UserDetailsServiceConfig {
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return UserDetailsService { email ->
            userRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("User not found with email: $email")
        }
    }
}
