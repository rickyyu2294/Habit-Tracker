package com.ricky.yu.habitTracker.services

import com.ricky.yu.habitTracker.context.RequestCtxHolder
import com.ricky.yu.habitTracker.models.HabitGroup
import com.ricky.yu.habitTracker.repositories.HabitGroupHabitRepository
import com.ricky.yu.habitTracker.repositories.HabitGroupRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class HabitGroupService(
    private val habitGroupRepository: HabitGroupRepository,
    private val userService: UserService,
    private val habitGroupHabitRepository: HabitGroupHabitRepository,
) {
    fun createGroup(
        name: String,
        isSystemGenerated: Boolean = false,
    ): HabitGroup {
        val user = userService.getUserById(RequestCtxHolder.get().userId)
        val habitGroup =
            HabitGroup(
                name = name,
                user = user,
                isSystemGenerated = isSystemGenerated,
            )
        return habitGroupRepository.save(habitGroup)
    }

    fun deleteGroup(groupId: Long) {
        val group = getGroupById(groupId)
        validateUserOwnsHabitGroup(groupId)
        require(!isSystemGenerated(groupId)) { "Cannot delete system generated group: group $groupId" }
        habitGroupRepository.delete(group)
    }

    fun isSystemGenerated(groupId: Long): Boolean {
        return habitGroupRepository.findIsSystemGeneratedByGroupId(groupId).orElseThrow {
            NoSuchElementException("Group Id $groupId doesn't exist")
        }
    }

    fun renameGroup(
        groupId: Long,
        newName: String,
    ): HabitGroup {
        val group = getGroupById(groupId)
        validateUserOwnsHabitGroup(groupId)
        require(!isSystemGenerated(groupId)) { "Cannot rename system generated group: group $groupId" }
        val updatedGroup = group.copy(name = newName)
        return habitGroupRepository.save(updatedGroup)
    }

    fun getGroups(): List<HabitGroup> {
        val userId = RequestCtxHolder.get().userId
        return habitGroupRepository.findAllByUserId(userId)
    }

    fun getGroupById(groupId: Long): HabitGroup {
        val userId = RequestCtxHolder.get().userId
        val group =
            habitGroupRepository.findByIdAndUserId(groupId, userId).orElseThrow {
                NoSuchElementException("No habit group $groupId for user $userId")
            }
        return group
    }

    fun getHabitIdsForGroup(groupId: Long): List<Long> {
        validateUserOwnsHabitGroup(groupId)
        return habitGroupHabitRepository.findByHabitGroup_IdOrderByOrderAsc(groupId).map { it.habit.id }
    }

    // helpers

    fun validateUserOwnsHabitGroup(groupId: Long) {
        val userId = RequestCtxHolder.get().userId
        val isOwner = habitGroupRepository.existsByIdAndUserId(groupId, userId)
        require(isOwner) { "User $userId does not own group $groupId" }
    }

    fun getLastOrderInGroup(groupId: Long): Int {
        validateUserOwnsHabitGroup(groupId)
        // Fetch the highest order value in the group
        return habitGroupHabitRepository.findTopByHabitGroup_IdOrderByOrderDesc(groupId)
            ?.order ?: -1 // Return -1 if no habits exist in the group
    }

    fun updateGroupOrdering(
        groupId: Long,
        habitIds: List<Long>,
    ) {
        validateUserOwnsHabitGroup(groupId)

        // Fetch the current habits in the group
        val groupHabits = habitGroupHabitRepository.findByHabitGroup_IdOrderByOrderAsc(groupId)
        val habitMap = groupHabits.associateBy { it.habit.id }

        // Validation
        require(habitIds.size == groupHabits.size) { "The number of provided habit IDs does not match the group size." }
        require(habitIds.toSet().size == habitIds.size) { "Duplicate habit ids not allowed in group update request" }
        habitIds.forEach { habitId ->
            require(habitMap.containsKey(habitId)) { "Habit ID $habitId does not belong to group $groupId." }
        }

        habitIds.forEachIndexed { index, habitId ->
            habitMap[habitId]?.order = index
        }

        habitGroupHabitRepository.saveAll(groupHabits)
    }

    fun getGroupByName(name: String): HabitGroup {
        return habitGroupRepository.findByNameAndUserId(name, RequestCtxHolder.get().userId)
    }
}
