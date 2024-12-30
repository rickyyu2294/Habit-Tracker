package com.ricky.yu.habitTracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HabitTrackerApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<HabitTrackerApplication>(*args)
}
