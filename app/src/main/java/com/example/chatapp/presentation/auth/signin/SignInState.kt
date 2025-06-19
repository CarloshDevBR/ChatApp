package com.example.chatapp.presentation.auth.signin

sealed interface SignInState {
    data object InvalidEmail : SignInState
    data object EmptyEmail : SignInState
    data object InvalidPassword : SignInState
    data object EmptyPassword : SignInState
    data object IsValidForm : SignInState
    data object Logged : SignInState
    data object Loading : SignInState

    data class SignInError(
        val error: String
    ) : SignInState
}