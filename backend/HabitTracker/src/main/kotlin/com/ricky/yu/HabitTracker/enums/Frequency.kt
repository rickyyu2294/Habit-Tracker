package com.ricky.yu.HabitTracker.enums

enum class Frequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
    CUSTOM
}

fun String.toFrequency(): Frequency {
    return when (this.uppercase()) {
        "DAILY" -> Frequency.DAILY
        "WEEKLY" -> Frequency.WEEKLY
        "MONTHLY" -> Frequency.MONTHLY
        "YEARLY" -> Frequency.YEARLY
        "CUSTOM" -> Frequency.CUSTOM
        else -> throw IllegalArgumentException("Invalid frequency: $this")
    }
}
