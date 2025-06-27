package com.example.chatapp.domain.usecase.user

import com.example.chatapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

interface LogoutUserUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}

class LogoutUserUseCaseImpl(
    private val repository: UserPreferencesRepository
) : LogoutUserUseCase {
    override suspend fun invoke(): Flow<Boolean> {
        return repository.removeUser()
    }
}