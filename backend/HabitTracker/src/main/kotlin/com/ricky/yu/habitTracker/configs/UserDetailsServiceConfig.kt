package com.ricky.yu.habitTracker.configs

import com.ricky.yu.habitTracker.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
class UserDetailsServiceConfig {
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return UserDetailsService { email ->
            userRepository.findByEmail(email).orElseThrow()
        }
    }
}
