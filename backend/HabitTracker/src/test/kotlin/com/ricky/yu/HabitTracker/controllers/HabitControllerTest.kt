package com.ricky.yu.HabitTracker.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ricky.yu.HabitTracker.BaseTest
import com.ricky.yu.HabitTracker.dtos.LoginRequest
import com.ricky.yu.HabitTracker.enums.Frequency
import com.ricky.yu.HabitTracker.repositories.HabitRepository
import com.ricky.yu.HabitTracker.repositories.UserRepository
import com.ricky.yu.HabitTracker.seeders.DataSeeder
import com.ricky.yu.HabitTracker.services.AuthService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class HabitControllerTest(): BaseTest() {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var dataSeeder: DataSeeder

    private lateinit var accessToken: String

    @BeforeAll
    fun login() {
        val loginResult = LoginRequest(
            email = DataSeeder.userEmail,
            password = DataSeeder.userPassword
        )
        accessToken = authService.authentication(loginResult).accessToken
    }

    @AfterAll
    fun setup() {
        dataSeeder.run()
    }

    @Test
    fun createHabit() {
        val input = mapOf(
            "name" to "Exercise",
            "description" to "Daily workout",
            "frequency" to "DAILY",
            "userId" to 1L
        )

        mockMvc.perform(
            post("/habits")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Exercise"))
            .andExpect(jsonPath("$.description").value("Daily workout"))
    }

    @Test
    fun getAllHabits() {
        val moo = mockMvc.perform(
            get("/habits")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value(DataSeeder.habitName))
    }

    @Test
    fun getHabitById() {
        mockMvc.perform(
            get("/habits/1")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(DataSeeder.habitName))
    }

    @Test
    fun updateHabit() {
        val input = HabitController.CreateHabitRequest(
            name = "Updated Habit",
            description = "updated habit description",
            frequency = Frequency.WEEKLY.name
        )

        mockMvc.perform(
            put("/habits/1")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Habit"))
    }

    @Test
    fun deleteHabit() {

    }
}