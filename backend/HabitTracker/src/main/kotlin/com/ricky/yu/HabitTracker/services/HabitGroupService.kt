package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.context.RequestCtxHolder
import com.ricky.yu.HabitTracker.models.HabitGroup
import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.repositories.HabitGroupRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class HabitGroupService(
    private val habitGroupRepository: HabitGroupRepository,
    private val userService: UserService
) {
    fun createGroup(name: String): HabitGroup {
        val userId = RequestCtxHolder.getRequestContext().userId
        val habitGroup = HabitGroup(
            name = name,
            user = userService.getUserById(userId)
        )
        return habitGroupRepository.save(habitGroup)
    }

    fun deleteGroup(groupId: Long) {
        val group = getGroupById(groupId)
        habitGroupRepository.delete(group)
    }

    fun renameGroup(groupId: Long, newName: String): HabitGroup {
        val group = getGroupById(groupId)

        val updatedGroup = group.copy(name = newName)
        return habitGroupRepository.save(updatedGroup)
    }

    fun getGroups(): List<HabitGroup> {
        val userId = RequestCtxHolder.getRequestContext().userId
        return habitGroupRepository.findAllByUserId(userId)
    }

    fun getGroupById(groupId: Long): HabitGroup {
        val userId = RequestCtxHolder.getRequestContext().userId
        val group = habitGroupRepository.findByIdAndUserId(groupId, userId).orElseThrow()
        return group
    }

    fun validateUserOwnsHabitGroup(groupId: Long) {
        val ctx = RequestCtxHolder.getRequestContext()
        val userId = ctx.userId
        val isOwner = habitGroupRepository.existsByIdAndUserId(groupId, userId)
        if (!isOwner) throw IllegalArgumentException("User $userId does not own group $groupId")
    }
}