package com.example.chatapp.domain.business

import com.example.chatapp.presentation.auth.signin.SignInEvent

interface SignInBusiness {
    fun isValidForm(email: String, password: String): SignInEvent
}