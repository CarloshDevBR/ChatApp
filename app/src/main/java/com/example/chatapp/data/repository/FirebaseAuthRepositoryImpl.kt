package com.example.chatapp.data.repository

import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.errors.AuthError
import com.example.chatapp.domain.repository.FireStoreAuthRepository
import com.example.chatapp.domain.repository.FirebaseAuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fireStoreAuthRepository: FireStoreAuthRepository
) : FirebaseAuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Flow<UserResponse> = flow {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw AuthError.UnknownError

            val userModel = toMapperSignInUserResponse(
                firebaseUser = firebaseUser,
                password = password
            )

            emit(userModel)
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
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw AuthError.UnknownError

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            firebaseUser.updateProfile(profileUpdates).await()

            val userModel = toMapperSignUpUserResponse(
                firebaseUser = firebaseUser,
                name = name,
                password = password
            )

            fireStoreAuthRepository.saveUser(userModel)

            emit(userModel)
        } catch (e: FirebaseException) {
            throw handlerError(e)
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

    private fun toMapperSignInUserResponse(
        firebaseUser: FirebaseUser,
        password: String
    ) = UserResponse(
        id = firebaseUser.uid,
        name = firebaseUser.displayName.orEmpty(),
        email = firebaseUser.email.orEmpty(),
        password = password,
        image = ""
    )

    private fun toMapperSignUpUserResponse(
        firebaseUser: FirebaseUser,
        name: String,
        password: String
    ) = UserResponse(
        id = firebaseUser.uid,
        name = firebaseUser.displayName ?: name,
        email = firebaseUser.email.orEmpty(),
        password = password,
        image = ""
    )

    private fun handlerError(exception: FirebaseException) = when (exception) {
        is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyInUse
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
        is FirebaseNetworkException -> AuthError.NetworkError
        else -> AuthError.UnknownError
    }
}