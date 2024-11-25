package com.ricky.yu.HabitTracker.models

import com.ricky.yu.HabitTracker.enums.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    private var password: String, // Internal backing field

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val habits: List<Habit> = mutableListOf()
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(GrantedAuthority { "ROLE_$role" })
    }
    
    @Transient
    override fun getPassword(): String = password // Map UserDetails method to the backing field

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
}