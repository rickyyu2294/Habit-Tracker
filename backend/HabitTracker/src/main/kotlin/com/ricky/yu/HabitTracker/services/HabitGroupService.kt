package com.ricky.yu.HabitTracker.services

import com.ricky.yu.HabitTracker.models.HabitGroup
import com.ricky.yu.HabitTracker.models.User
import com.ricky.yu.HabitTracker.repositories.HabitGroupRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class HabitGroupService(
    private val habitGroupRepository: HabitGroupRepository
) {
    fun createGroup(name: String, user: User): HabitGroup {
        val habitGroup = HabitGroup(
            name = name,
            user = user
        )
        return habitGroupRepository.save(habitGroup)
    }

    fun deleteGroup(groupId: Long, user: User) {
        val group = habitGroupRepository.findById(groupId)
            .orElseThrow { NoSuchElementException("HabitGroup with ID $groupId not found.") }

        if (group.user.id != user.id) {
            throw IllegalArgumentException("Cannot delete a group that doesn't belong to the user.")
        }

        habitGroupRepository.delete(group)
    }

    fun renameGroup(groupId: Long, newName: String, user: User): HabitGroup {
        val group = habitGroupRepository.findById(groupId)
            .orElseThrow { NoSuchElementException("HabitGroup with ID $groupId not found.") }

        if (group.user.id != user.id) {
            throw IllegalArgumentException("Cannot rename a group that doesn't belong to the user.")
        }

        val updatedGroup = group.copy(name = newName)
        return habitGroupRepository.save(updatedGroup)
    }

    fun getGroupsForUser(user: User): List<HabitGroup> {
        return habitGroupRepository.findAllByUserId(user.id)
    }
}