package com.example.chatapp.presentation.home

sealed interface HomeEvent {
    data object LoggedOut : HomeEvent
}