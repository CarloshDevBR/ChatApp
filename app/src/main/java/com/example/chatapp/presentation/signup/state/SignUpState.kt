package com.example.chatapp.presentation.signup.state

sealed interface SignUpState {
    data object InitialState : SignUpState
    data object Registered : SignUpState
}