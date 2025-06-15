package com.example.chatapp.domain.datasource

import com.example.chatapp.core.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface SharedPreferencesDataSource {
    fun save(key: String, value: UserResponse): Flow<Boolean>

    fun get(key: String): Flow<UserResponse>

    fun remove(key: String): Flow<Boolean>
}