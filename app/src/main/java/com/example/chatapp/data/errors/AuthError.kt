package com.example.chatapp.data.errors

sealed class AuthError : Exception() {
    data object UserNotFound : AuthError()
    data object InvalidCredentials : AuthError()
    data object UnknownError : AuthError()
}