package com.example.chatapp.core.util

sealed class ResultData<out T> {
    data class Success<out T>(val data: T?) : ResultData<T>()

    data class Failure(val message: String) : ResultData<Nothing>()

    data object Loading : ResultData<Nothing>()
}