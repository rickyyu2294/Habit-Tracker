package com.ricky.yu.HabitTracker.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtTokenService(
    @Value("\${jwt.secret}")
    private val secret: String = ""
) {
    private val signingKey: SecretKeySpec
        get() {
            val keyBytes: ByteArray = Base64.getDecoder().decode(secret)
            return SecretKeySpec(keyBytes, 0, keyBytes.size, "HmacSHA256")
        }

    fun generateToken(subject: String, expiration: Date, additionalClaims: Map<String, Any> = emptyMap()): String {
        return Jwts.builder()
            .setClaims(additionalClaims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expiration)
            .signWith(signingKey)
            .compact()
    }

    fun extractEmail(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractClaim(token: String, claim: String): String {
        return extractAllClaims(token)[claim] as String
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}