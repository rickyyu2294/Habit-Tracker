package com.ricky.yu.HabitTracker.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {
    private val secret = "mySecretKey" // Replace with a strong secret key
    private val expirationMs: Long = 3600000 // Token validity (1 hour)

    fun generateToken(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(SignatureAlgorithm.HS256, secret.toByteArray())
            .compact()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secret.toByteArray()).parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun getEmailFromToken(token: String): String {
        val claims: Claims = Jwts.parser().setSigningKey(secret.toByteArray()).parseClaimsJws(token).body
        return claims.subject
    }
}
