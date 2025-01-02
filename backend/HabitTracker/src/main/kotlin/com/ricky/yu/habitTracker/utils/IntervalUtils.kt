package com.ricky.yu.habitTracker.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields

object IntervalUtils {
    val dailyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val weeklyFormatter = DateTimeFormatter.ofPattern("yyyy-'W'ww")

    val monthlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    fun dailyIntervalToRange(interval: String): Pair<LocalDateTime, LocalDateTime> {
        val date = LocalDate.parse(interval, dailyFormatter) // Parse as LocalDate
        val start = date.atStartOfDay() // Start of the day
        val end = date.plusDays(1).atStartOfDay() // Start of the next day
        return Pair(start, end)
    }

    fun weeklyIntervalToRange(interval: String): Pair<LocalDateTime, LocalDateTime> {
        val year = interval.substring(0, 4).toInt() // Extract year
        val week = interval.substring(6).toInt() // Extract week number

        val startDate =
            LocalDate.ofYearDay(year, 1)
                .with(WeekFields.SUNDAY_START.weekOfYear(), week.toLong())
                .with(WeekFields.SUNDAY_START.dayOfWeek(), 1)

        val start = startDate.atStartOfDay()
        val end = startDate.plusWeeks(1).atStartOfDay()
        return Pair(start, end)
    }

    fun monthlyIntervalToRange(interval: String): Pair<LocalDateTime, LocalDateTime> {
        val yearMonth = YearMonth.parse(interval, monthlyFormatter) // Parse as YearMonth
        val start = yearMonth.atDay(1).atStartOfDay() // Start of the month
        val end = yearMonth.plusMonths(1).atDay(1).atStartOfDay() // Start of the next month
        return Pair(start, end)
    }
}
