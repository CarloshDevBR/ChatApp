package com.example.chatapp.data.repository

import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.data.errors.AuthError
import com.example.chatapp.domain.repository.FireStoreAuthRepository
import com.example.chatapp.domain.repository.FirebaseAuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStoreAuthRepository: FireStoreAuthRepository
) : FirebaseAuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Flow<AuthResult> = flow {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            emit(result)
        } catch (e: FirebaseException) {
            throw handlerError(e)
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Flow<UserResponse> = flow {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                firebaseUser.updateProfile(profileUpdates).await()

                val userModel = UserResponse(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName ?: name,
                    email = firebaseUser.email ?: "",
                    password = password,
                    image = ""
                )

                fireStoreAuthRepository.saveUser(userModel)
                    .catch {
                        throw it
                    }
                    .collect {
                        emit(userModel)
                    }
            }
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun signOut(): Flow<Boolean> = flow {
        try {
            firebaseAuth.signOut()

            emit(true)
        } catch (e: FirebaseException) {
            throw e
        }
    }

    private fun handlerError(exception: FirebaseException): AuthError = when (exception) {
        is FirebaseAuthInvalidUserException -> AuthError.UserNotFound
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
        else -> AuthError.UnknownError
    }
}