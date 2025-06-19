package com.example.chatapp.domain.errors

sealed class AuthError : Exception() {
    data object EmailAlreadyInUse : AuthError()
    data object InvalidCredentials : AuthError()
    data object NetworkError : AuthError()
    data object UnknownError : AuthError()
}