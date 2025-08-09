package com.example.chatapp.domain.business

import android.util.Patterns
import com.example.chatapp.presentation.auth.signup.SignUpEvent

class SignUpBusinessImpl : SignUpBusiness {
    override fun isValidateForm(name: String, email: String, password: String): SignUpEvent {
        return when {
            validName(name).not() -> SignUpEvent.InvalidName
            validEmail(email).not() -> SignUpEvent.InvalidEmail
            email.isEmpty() -> SignUpEvent.EmptyEmail
            validPassword(password).not() -> SignUpEvent.InvalidPassword
            password.isEmpty() -> SignUpEvent.EmptyPassword
            else -> SignUpEvent.IsValidForm
        }
    }

    private fun validName(name: String): Boolean = name.isNotEmpty()

    private fun validEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validPassword(password: String): Boolean = password.length >= MINIMUM_PASSWORD

    private companion object {
        const val MINIMUM_PASSWORD = 8
    }
}