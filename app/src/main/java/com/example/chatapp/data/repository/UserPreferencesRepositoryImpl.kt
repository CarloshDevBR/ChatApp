package com.example.chatapp.data.repository

import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.domain.datasource.SharedPreferencesDataSource
import com.example.chatapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl(
    private val dataSource: SharedPreferencesDataSource
) : UserPreferencesRepository {
    override suspend fun saveUser(user: UserResponse): Flow<Boolean> = dataSource.save(KEY, user)

    override suspend fun getUser(): Flow<UserResponse> = dataSource.get(KEY)

    override suspend fun removeUser(): Flow<Boolean> = dataSource.remove(KEY)

    private companion object {
        const val KEY = "user_access"
    }
}