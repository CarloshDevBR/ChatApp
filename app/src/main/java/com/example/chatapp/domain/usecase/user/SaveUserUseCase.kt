package com.example.chatapp.domain.usecase.user

import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.domain.repository.UserPreferencesRepository
import com.example.chatapp.domain.usecase.user.SaveUserUseCase.Params
import kotlinx.coroutines.flow.Flow

interface SaveUserUseCase {
    suspend operator fun invoke(params: Params): Flow<Boolean>

    data class Params(val user: UserResponse)
}

class SaveUserUseCaseImpl(
    private val repository: UserPreferencesRepository
) : SaveUserUseCase {
    override suspend fun invoke(params: Params): Flow<Boolean> {
        return repository.saveUser(params.user)
    }
}