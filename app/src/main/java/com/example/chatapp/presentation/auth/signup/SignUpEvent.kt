package com.example.chatapp.presentation.auth.signup

sealed interface SignUpEvent {
    data object Loading : SignUpEvent
    data object InvalidName : SignUpEvent
    data object InvalidEmail : SignUpEvent
    data object EmptyEmail : SignUpEvent
    data object InvalidPassword : SignUpEvent
    data object EmptyPassword : SignUpEvent
    data object IsValidForm : SignUpEvent
    data class PasswordVisible(val visible: Boolean = false) : SignUpEvent

    data class SignUpError(
        val error: String
    ) : SignUpEvent
}