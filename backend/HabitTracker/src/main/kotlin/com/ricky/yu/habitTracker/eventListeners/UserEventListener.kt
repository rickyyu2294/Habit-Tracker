package com.ricky.yu.habitTracker.eventListeners

import com.ricky.yu.habitTracker.context.RequestCtx
import com.ricky.yu.habitTracker.context.usingTempCtx
import com.ricky.yu.habitTracker.models.User
import com.ricky.yu.habitTracker.services.HabitGroupService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserEventListener(
    private val habitGroupService: HabitGroupService,
) {
    @EventListener
    fun handleUserRegistered(event: UserRegisteredEvent) {
        usingTempCtx(
            RequestCtx(
                userId = event.user.id,
                role = event.user.role,
                requestId = UUID.randomUUID().toString(),
            ),
        ) {
            habitGroupService.createGroup(name = HabitGroupService.ALL_GROUP_NAME, isSystemGenerated = true)
        }
    }
}

data class UserRegisteredEvent(val user: User)
