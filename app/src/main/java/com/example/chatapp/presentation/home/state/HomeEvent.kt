package com.example.chatapp.presentation.home.state

sealed interface HomeEvent {
    data object NavigateToProfile : HomeEvent
    data object LoggedOut : HomeEvent
}