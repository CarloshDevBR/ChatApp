package com.example.chatapp.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.chatapp.data.model.response.UserResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPreferencesDataSourceImpl(
    context: Context
) : SharedPreferencesDataSource {
    private val security: SharedPreferences = context.applicationContext.getSharedPreferences(
        SHARED_NAME, Context.MODE_PRIVATE
    )

    private val gson = Gson()

    override fun save(key: String, value: UserResponse): Flow<Boolean> = flow {
        val json = gson.toJson(value)
        val result = security.edit().putString(key, json).commit()
        emit(result)
    }

    override fun get(key: String): Flow<UserResponse> = flow {
        val json = security.getString(key, null)
        val user = json?.let {
            gson.fromJson(it, UserResponse::class.java)
        } ?: UserResponse()
        emit(user)
    }

    override fun remove(key: String): Flow<Boolean> = flow {
        val result = security.edit().remove(key).commit()
        emit(result)
    }

    private companion object {
        const val SHARED_NAME = "chat_user"
    }
}