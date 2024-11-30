package com.ricky.yu.HabitTracker.controllers

import com.ricky.yu.HabitTracker.BaseTest
import com.ricky.yu.HabitTracker.repositories.UserRepository
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.print.attribute.standard.Media

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class AuthControllerTest: BaseTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository.deleteAll() // Clean up the database before each test
    }

    @Test
    fun `should register a new user`() {
        registerUser(mockMvc, registerTestUserObject)
            .andExpect(status().isOk)
            .andExpect(content().string("Registration successful"))

        val user = userRepository.findByEmail("test@test.com")
        assertNotNull(user)
        assertEquals("Test User", user?.name)
    }

    @Test
    fun `should fail to register a duplicate email`() {
        userRepository.save(testUser)
        registerUser(mockMvc, registerTestUserObject)
            .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().string("Email is already registered"))
    }

    @Test
    fun `should fail to register without email specified`() {
        val registerObject = JSONObject()
            .put("password", "testPassword")
            .put("name", "No Email")

        registerUser(mockMvc, registerObject)
            .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
    }

    @Test
    fun `should fail to register without password specified`() {
        val registerObject = JSONObject()
            .put("email", "test@test.com")
            .put("name", "No Password")

        registerUser(mockMvc, registerObject)
            .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
    }

    @Test
    fun `should login successfully for valid user`() {
        userRepository.save(testUser)
        val result = login(mockMvc, loginTestUserObject)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").isNotEmpty)
            .andExpect(jsonPath("$.refreshToken").isNotEmpty)
            .andReturn()

        val responseContent = result.response.contentAsString
        val responseJson = JSONObject(responseContent)
        val accessToken = responseJson.getString("accessToken")
        val refreshToken = responseJson.getString("refreshToken")

        assert(!accessToken.isNullOrEmpty())
        assert(!refreshToken.isNullOrEmpty())
    }

    @Test
    fun `should fail login for invalid user`() {
        userRepository.save(testUser)
        val loginObject = JSONObject()
            .put("email", "wrong@test.com")
            .put("password", "wrongPassword")
        login(mockMvc, loginObject)
            .andExpect(status().`is`(HttpStatus.UNAUTHORIZED.value()))
            .andExpect(content().string("Bad credentials"))
    }

    @Test
    fun `should fail to login without email`() {
        userRepository.save(testUser)
        val loginObject = JSONObject()
            .put("password", "testPassword")
        login(mockMvc, loginObject)
            .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
    }

    @Test
    fun `should fail to login without password`() {
        userRepository.save(testUser)
        val loginObject = JSONObject()
            .put("email", "test@test.com")
        login(mockMvc, loginObject)
            .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
    }


    @Test
    fun `should logout valid user successfully`() {
        // register and login
        registerUser(mockMvc, registerTestUserObject)
        val loginResult = login(mockMvc, loginTestUserObject).andReturn()
        val responseContent = loginResult.response.contentAsString
        val responseJson = JSONObject(responseContent)
        val accessToken = responseJson.getString("accessToken")
        val refreshToken = responseJson.getString("refreshToken")

        val logoutInput = JSONObject()
            .put("accessToken", accessToken)
            .put("refreshToken", refreshToken)

        logout(mockMvc, logoutInput, accessToken)
            .andExpect(status().isOk)
            .andExpect(content().string("Logout Successful"))
    }

    @Test
    fun `should fail logout for invalid request`() {
        registerUser(mockMvc, registerTestUserObject)
        val loginResult = login(mockMvc, loginTestUserObject).andReturn()
        val responseContent = loginResult.response.contentAsString
        val responseJson = JSONObject(responseContent)
        val accessToken = responseJson.getString("accessToken")

        val invalidInput = JSONObject()
            .put("accessToken", accessToken)
            .put("refreshToken", "invalid refresh token")

        logout(mockMvc, invalidInput, accessToken)
            .andExpect(status().isBadRequest)
    }

    // TODO: add refresh access token tests

    fun registerUser(mockMvc: MockMvc, registerInput: JSONObject): ResultActions {
        return mockMvc.perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerInput.toString())
        )
    }

    fun login(mockMvc: MockMvc, loginInput: JSONObject): ResultActions {
        return mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginInput.toString())
        )
    }

    fun logout(mockMvc: MockMvc, logoutInput: JSONObject, accessToken: String): ResultActions {
        return mockMvc.perform(
            post("/logout-app")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutInput.toString())
        )
    }

    companion object {
        val registerTestUserObject: JSONObject = JSONObject()
            .put("email", "test@test.com")
            .put("password", "testPassword")
            .put("name", "Test User")

        val loginTestUserObject = JSONObject()
            .put("email", "test@test.com")
            .put("password", "testPassword")
    }
}