package com.example.chatapp.data.repository

import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.domain.datasource.SharedPreferencesDataSource
import com.example.chatapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserPreferencesRepositoryImpl(
    private val dataSource: SharedPreferencesDataSource
) : UserPreferencesRepository {
    override suspend fun saveUser(user: UserResponse): Flow<Boolean> = flow {
        try {
            val result = dataSource.save(KEY, user)

            emit(result)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(): Flow<UserResponse> = flow {
        try {
            val result = dataSource.get(KEY)

            emit(result)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun removeUser(): Flow<Boolean> = flow {
        try {
            val result = dataSource.remove(KEY)

            emit(result)
        } catch (e: Exception) {
            throw e
        }
    }

    private companion object {
        const val KEY = "user_access"
    }
}