package com.example.chatapp.domain.repository

import com.example.chatapp.core.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface FireStoreAuthRepository {
    suspend fun saveUser(user: UserResponse): Flow<Boolean>
}