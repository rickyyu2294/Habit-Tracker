package com.ricky.yu.habitTracker.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.ricky.yu.habitTracker.enums.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Transient
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val email: String,
    // Internal backing field
    @Column(nullable = false)
    private var password: String,
    @Column(nullable = false)
    val name: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    val habits: List<Habit> = mutableListOf(),
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    val refreshTokens: List<RefreshToken> = mutableListOf(),
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(GrantedAuthority { "ROLE_$role" })
    }

    // Map UserDetails method to the backing field
    @Transient
    override fun getPassword(): String = password

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email')"
    }
}
