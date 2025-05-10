package com.example.chatapp.domain.repository

import com.example.chatapp.core.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun signIn(email: String, password: String): Flow<UserResponse>
    suspend fun signUp(name: String, email: String, password: String): Flow<UserResponse>
    suspend fun signOut(): Flow<Boolean>
}