package com.example.chatapp.domain.business

import com.example.chatapp.presentation.auth.signup.SignUpState

interface SignUpBusiness {
    fun isValidateForm(
        name: String,
        email: String,
        password: String,
    ): SignUpState
}