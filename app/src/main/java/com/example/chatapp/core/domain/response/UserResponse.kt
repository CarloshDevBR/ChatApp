package com.example.chatapp.core.domain.response

data class UserResponse(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val image: String = ""
)