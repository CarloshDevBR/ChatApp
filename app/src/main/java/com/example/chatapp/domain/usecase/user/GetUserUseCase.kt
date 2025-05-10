package com.example.chatapp.domain.usecase.user

import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

interface GetUserUseCase {
    suspend operator fun invoke(): Flow<UserResponse>
}

class GetUserUseCaseImpl(
    private val repository: UserPreferencesRepository
) : GetUserUseCase {
    override suspend fun invoke(): Flow<UserResponse> {
        return repository.getUser()
    }
}