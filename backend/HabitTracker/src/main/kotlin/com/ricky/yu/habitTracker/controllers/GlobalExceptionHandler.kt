package com.ricky.yu.habitTracker.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage.orEmpty() }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<Any> {
        println(ex.message)
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(SQLException::class)
    fun handleSQLException(ex: SQLException): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(ex.message)
    }
}
