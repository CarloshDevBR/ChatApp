package com.example.chatapp.domain.errors

sealed class AuthError : Exception() {
    data object UserNotFound : AuthError()
    data object InvalidCredentials : AuthError()
    data object UnknownError : AuthError()
    data object WeakPassword : AuthError()
    data object InvalidEmail : AuthError()
    data object EmailAlreadyInUse : AuthError()
    data object NetworkError : AuthError()
}