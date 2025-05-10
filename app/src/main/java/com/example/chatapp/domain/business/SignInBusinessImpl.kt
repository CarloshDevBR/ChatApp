package com.example.chatapp.domain.business

import android.util.Patterns
import com.example.chatapp.presentation.auth.signin.SignInState

class SignInBusinessImpl : SignInBusiness {
    override fun isValidForm(email: String, password: String): SignInState {
        return when {
            email.isEmpty() -> SignInState.EmptyEmail
            validEmail(email).not() -> SignInState.InvalidEmail
            password.isEmpty() -> SignInState.EmptyPassword
            validPassword(password).not() -> SignInState.InvalidPassword
            else -> SignInState.IsValidForm
        }
    }

    private fun validEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validPassword(password: String): Boolean = password.length >= MINIMUM_PASSWORD

    private companion object {
        const val MINIMUM_PASSWORD = 8
    }
}