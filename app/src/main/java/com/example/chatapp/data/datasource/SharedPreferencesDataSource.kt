package com.example.chatapp.data.datasource

import com.example.chatapp.data.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface SharedPreferencesDataSource {
    fun save(key: String, value: UserResponse): Flow<Boolean>

    fun get(key: String): Flow<UserResponse>

    fun remove(key: String): Flow<Boolean>
}