package com.example.chatapp.domain.datasource

import com.example.chatapp.core.domain.response.UserResponse

interface SharedPreferencesDataSource {
    fun save(key: String, value: UserResponse): Boolean

    fun get(key: String): UserResponse

    fun remove(key: String): Boolean
}