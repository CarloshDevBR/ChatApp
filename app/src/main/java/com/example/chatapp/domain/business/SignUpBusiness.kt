package com.example.chatapp.domain.business

import com.example.chatapp.presentation.signup.state.SignUpEvent

interface SignUpBusiness {
    fun isValidateForm(
        name: String,
        email: String,
        password: String,
    ): SignUpEvent
}