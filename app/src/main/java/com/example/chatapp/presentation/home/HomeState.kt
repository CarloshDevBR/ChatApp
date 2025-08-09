package com.example.chatapp.presentation.home

import com.example.chatapp.data.model.response.UserResponse

sealed interface HomeState {
    data class User(val data: UserResponse) : HomeState
}