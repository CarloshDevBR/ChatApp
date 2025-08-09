package com.example.chatapp.presentation.auth.signin

sealed interface SignInState {
    data object InitialState : SignInState
    data object Logged : SignInState
}