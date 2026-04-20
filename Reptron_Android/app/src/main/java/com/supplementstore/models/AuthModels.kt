package com.supplementstore.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val userName: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

data class AuthResponse(
    val token: String,
    val email: String,
    val userName: String,
    val role: String,
    val userId: String
)