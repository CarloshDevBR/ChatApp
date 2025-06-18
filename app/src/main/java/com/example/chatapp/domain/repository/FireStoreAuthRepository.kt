package com.example.chatapp.domain.repository

import com.example.chatapp.data.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface FireStoreAuthRepository {
    suspend fun saveUser(user: UserResponse): Flow<Boolean>
}