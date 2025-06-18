package com.example.chatapp.domain.repository

import com.example.chatapp.data.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveUser(user: UserResponse): Flow<Boolean>
    suspend fun getUser(): Flow<UserResponse>
    suspend fun removeUser(): Flow<Boolean>
}