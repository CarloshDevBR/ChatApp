package com.example.chatapp.data.repository

import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.domain.repository.FireStoreAuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FireStoreAuthRepositoryImpl(
    private val firestore: FirebaseFirestore
) : FireStoreAuthRepository {
    override suspend fun saveUser(user: UserResponse): Flow<Boolean> = flow {
        try {
            firestore
                .collection(COLLECTION)
                .document(user.id)
                .set(user)
                .await()

            emit(true)
        } catch (e: FirebaseException) {
            throw e
        }
    }

    private companion object {
        const val COLLECTION = "users"
    }
}