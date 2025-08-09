package com.example.chatapp.presentation.auth.signup

sealed interface SignUpState {
    data object InitialState : SignUpState
    data object Registered : SignUpState
}