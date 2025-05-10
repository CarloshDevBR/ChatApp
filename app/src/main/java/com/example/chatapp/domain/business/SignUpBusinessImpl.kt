package com.example.chatapp.domain.business

import android.util.Patterns
import com.example.chatapp.presentation.auth.signup.SignUpState

class SignUpBusinessImpl : SignUpBusiness {
    override fun isValidateForm(name: String, email: String, password: String): SignUpState {
        return when {
            validName(name).not() -> SignUpState.InvalidName
            validEmail(email).not() -> SignUpState.InvalidEmail
            email.isEmpty() -> SignUpState.EmptyEmail
            validPassword(password).not() -> SignUpState.InvalidPassword
            password.isEmpty() -> SignUpState.EmptyPassword
            else -> SignUpState.IsValidForm
        }
    }

    private fun validName(name: String): Boolean = name.isNotEmpty()

    private fun validEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validPassword(password: String): Boolean = password.length >= MINIMUM_PASSWORD

    private companion object {
        const val MINIMUM_PASSWORD = 8
    }
}