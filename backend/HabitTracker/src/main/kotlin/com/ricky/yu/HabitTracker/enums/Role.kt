package com.ricky.yu.HabitTracker.enums

enum class Role {
    ADMIN,
    USER
}

fun String.toRole(): Role {
    return when(this.uppercase()) {
        "ADMIN" -> Role.ADMIN
        "USER" -> Role.USER
        else -> throw IllegalArgumentException("Invalid user role: $this")
    }
}