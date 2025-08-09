package com.example.chatapp.domain.business

import android.util.Patterns
import com.example.chatapp.presentation.auth.signin.SignInEvent

class SignInBusinessImpl : SignInBusiness {
    override fun isValidForm(email: String, password: String): SignInEvent {
        return when {
            email.isEmpty() -> SignInEvent.EmptyEmail
            validEmail(email).not() -> SignInEvent.InvalidEmail
            password.isEmpty() -> SignInEvent.EmptyPassword
            validPassword(password).not() -> SignInEvent.InvalidPassword
            else -> SignInEvent.IsValidForm
        }
    }

    private fun validEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validPassword(password: String): Boolean = password.length >= MINIMUM_PASSWORD

    private companion object {
        const val MINIMUM_PASSWORD = 8
    }
}