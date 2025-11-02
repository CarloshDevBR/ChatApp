package com.example.chatapp.domain.business

import com.example.chatapp.presentation.signin.state.SignInEvent

interface SignInBusiness {
    fun isValidForm(email: String, password: String): SignInEvent
}