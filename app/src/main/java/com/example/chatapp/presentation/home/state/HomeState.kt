package com.example.chatapp.presentation.home.state

sealed interface HomeState {
    data class Tabs(val tabs: Map<Int, String>) : HomeState
    data object LoggedOut : HomeState
}