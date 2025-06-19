package com.example.chatapp.presentation.auth.signup

sealed interface SignUpState {
    data object Loading : SignUpState
    data object InvalidName : SignUpState
    data object InvalidEmail : SignUpState
    data object EmptyEmail : SignUpState
    data object InvalidPassword : SignUpState
    data object EmptyPassword : SignUpState
    data object IsValidForm : SignUpState
    data object Subscribed : SignUpState

    data class SignUpError(
        val error: String
    ) : SignUpState
}