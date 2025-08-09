package com.example.chatapp.presentation.auth.signin

sealed interface SignInEvent {
    data object Loading : SignInEvent
    data object InvalidEmail : SignInEvent
    data object EmptyEmail : SignInEvent
    data object InvalidPassword : SignInEvent
    data object EmptyPassword : SignInEvent
    data object IsValidForm : SignInEvent
    data class PasswordVisible(val visible: Boolean) : SignInEvent

    data class SignInError(
        val error: String
    ) : SignInEvent
}