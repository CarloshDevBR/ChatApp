package com.example.chatapp.domain.repository

import com.example.chatapp.data.model.response.UserResponse

interface FireStoreAuthRepository {
    suspend fun saveUser(user: UserResponse)
}