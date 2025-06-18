package com.example.chatapp.domain.usecase.auth

import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface SignInUseCase {
    suspend operator fun invoke(params: Params): Flow<UserResponse>

    data class Params(val email: String, val password: String)
}

class SignInUseCaseImpl(
    private val repository: FirebaseAuthRepository
) : SignInUseCase {
    override suspend fun invoke(params: SignInUseCase.Params): Flow<UserResponse> {
        return repository.signIn(params.email, params.password)
    }
}