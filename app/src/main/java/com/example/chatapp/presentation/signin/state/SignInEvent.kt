package com.example.chatapp.presentation.signin.state

sealed interface SignInEvent {
    data object Loading : SignInEvent
    data object InvalidEmail : SignInEvent
    data object EmptyEmail : SignInEvent
    data object InvalidPassword : SignInEvent
    data object EmptyPassword : SignInEvent
    data object IsValidForm : SignInEvent
    data class PasswordIsVisible(val isVisible: Boolean = false) : SignInEvent

    data class SignInError(
        val error: String
    ) : SignInEvent
}