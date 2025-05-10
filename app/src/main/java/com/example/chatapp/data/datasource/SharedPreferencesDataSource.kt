package com.example.chatapp.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.domain.datasource.SharedPreferencesDataSource
import com.google.gson.Gson

class SharedPreferencesDataSourceImpl(context: Context) : SharedPreferencesDataSource {
    private val security: SharedPreferences = context.getSharedPreferences(
        SHARED_NAME,
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    override fun save(key: String, value: UserResponse): Boolean {
        val json = gson.toJson(value)
        return security.edit().putString(key, json).commit()
    }

    override fun get(key: String): UserResponse {
        val json = security.getString(key, null)
        return json.let {
            gson.fromJson(it, UserResponse::class.java)
        } ?: UserResponse()
    }

    override fun remove(key: String): Boolean = security.edit().remove(key).commit()

    private companion object {
        const val SHARED_NAME = "chat_user"
    }
}