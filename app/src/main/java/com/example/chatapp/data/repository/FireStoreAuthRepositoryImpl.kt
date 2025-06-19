package com.example.chatapp.data.repository

import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.repository.FireStoreAuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireStoreAuthRepositoryImpl(
    private val firestore: FirebaseFirestore
) : FireStoreAuthRepository {
    override suspend fun saveUser(user: UserResponse) {
        firestore
            .collection(COLLECTION)
            .document(user.id)
            .set(user)
            .await()
    }

    private companion object {
        const val COLLECTION = "users"
    }
}