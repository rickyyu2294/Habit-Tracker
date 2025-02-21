package com.ricky.yu.habitTracker.seeders

import com.ricky.yu.habitTracker.context.RequestCtx
import com.ricky.yu.habitTracker.context.usingTempCtx
import com.ricky.yu.habitTracker.controllers.HabitController
import com.ricky.yu.habitTracker.enums.IntervalType
import com.ricky.yu.habitTracker.services.HabitCompletionService
import com.ricky.yu.habitTracker.services.HabitGroupService
import com.ricky.yu.habitTracker.services.HabitService
import com.ricky.yu.habitTracker.services.UserService
import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
@Profile("dev", "test")
@Suppress("MagicNumber", "MayBeConst")
class DataSeeder(
    private val userService: UserService,
    private val habitService: HabitService,
    private val habitCompletionService: HabitCompletionService,
    private val habitGroupService: HabitGroupService,
) : CommandLineRunner {
    @Transactional
    @Suppress("LongMethod")
    override fun run(vararg args: String?) {
        // Check if the test user already exists
        userService.registerUser(USER_EMAIL, USER_PASSWORD, USER_NAME)
        val user = userService.getUserByEmail(USER_EMAIL)

        usingTempCtx(
            RequestCtx(
                userId = user.id,
                role = user.role,
                requestId = UUID.randomUUID().toString(),
            ),
        ) {
            // Create Groups
            val habitGroup = habitGroupService.createGroup("Test Group")

            // Create Habits
            val climb =
                habitService.createHabit(
                    HabitController.CreateHabitRequest(
                        name = "Climb",
                        description = "Gotta climb",
                        interval = IntervalType.WEEKLY.name,
                        frequency = 3,
                        groupIds = listOf(habitGroup.id),
                    ),
                )

            val meditate =
                habitService.createHabit(
                    HabitController.CreateHabitRequest(
                        name = "Meditate",
                        description = "Gotta meditate",
                        interval = IntervalType.DAILY.name,
                        frequency = 1,
                        groupIds = listOf(habitGroup.id),
                    ),
                )

            val bills =
                habitService.createHabit(
                    HabitController.CreateHabitRequest(
                        name = "Pay Bills",
                        description = "Gotta pay bills",
                        interval = IntervalType.MONTHLY.name,
                        frequency = 1,
                    ),
                )

            habitGroupService.updateGroupOrdering(habitGroup.id, listOf(meditate.id, climb.id))
            habitGroupService.updateGroupOrdering(
                habitGroupService
                    .getGroupByName(
                        HabitGroupService.ALL_GROUP_NAME
                    ).id,
                listOf(bills.id, climb.id, meditate.id)
            )

            habitCompletionService.createCompletion(
                habitId = climb.id,
                dateTime = LocalDateTime.now().minusWeeks(3),
            )

            habitCompletionService.createCompletion(
                habitId = climb.id,
                dateTime = LocalDateTime.now().minusWeeks(1),
            )

            habitCompletionService.createCompletion(
                habitId = climb.id,
                dateTime = LocalDateTime.now().minusWeeks(1).minusDays(1),
            )

            habitCompletionService.createCompletion(
                habitId = meditate.id,
                dateTime = LocalDateTime.now().minusDays(3),
            )

            habitCompletionService.createCompletion(
                habitId = bills.id,
                dateTime = LocalDateTime.now().minusMonths(3),
            )
        }
    }

    companion object {
        const val USER_EMAIL = "test.user@test.com"
        const val USER_NAME = "Test User"
        const val USER_PASSWORD = "testPassword"
    }
}
