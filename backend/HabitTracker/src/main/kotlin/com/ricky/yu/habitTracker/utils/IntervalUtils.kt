package com.ricky.yu.habitTracker.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields

object IntervalUtils {
    private const val DAILY_FORMAT = "yyyy-MM-dd"
    private const val WEEKLY_FORMAT = "YYYY-'W'ww"
    private const val MONTHLY_FORMAT = "yyyy-MM"
    private val weekFields = WeekFields.SUNDAY_START

    val dailyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DAILY_FORMAT)

    val weeklyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(WEEKLY_FORMAT)

    val monthlyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(MONTHLY_FORMAT)

    fun dailyIntervalToRange(interval: String): Pair<LocalDateTime, LocalDateTime> {
        val start = getEarliestDateTimeInDailyInterval(interval)
        val end = start.plusDays(1)
        return Pair(start, end)
    }

    fun weeklyIntervalToRange(interval: String): Pair<LocalDateTime, LocalDateTime> {
        val start = getEarliestDateTimeInWeeklyInterval(interval)
        val end = start.plusWeeks(1)
        return Pair(start, end)
    }

    fun monthlyIntervalToRange(interval: String): Pair<LocalDateTime, LocalDateTime> {
        val start = getEarliestDateTimeInMonthlyInterval(interval)
        val end = start.plusMonths(1)
        return Pair(start, end)
    }

    fun getEarliestDateTimeInDailyInterval(interval: String): LocalDateTime {
        val date = LocalDate.parse(interval, dailyFormatter)
        return date.atStartOfDay()
    }

    fun getEarliestDateTimeInWeeklyInterval(interval: String): LocalDateTime {
        val yearEndIndex = 4
        val weekEndIndex = 6
        val year = interval.substring(0, yearEndIndex).toInt()
        val week = interval.substring(weekEndIndex).toInt()
        val startDate =
            LocalDate.ofYearDay(year, 1)
                // Currently hardcoding sunday_start
                .with(weekFields.weekOfYear(), week.toLong())
                .with(weekFields.dayOfWeek(), 1)

        return startDate.atStartOfDay()
    }

    fun getEarliestDateTimeInMonthlyInterval(interval: String): LocalDateTime {
        val yearMonth = YearMonth.parse(interval, monthlyFormatter)
        val startDate = yearMonth.atDay(1)
        return startDate.atStartOfDay()
    }
}
