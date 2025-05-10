package com.example.chatapp.navigation

import androidx.navigation.NavDirections
import com.example.chatapp.presentation.auth.signup.SignUpFragmentDirections

class SignUpNavigationImpl : SignUpNavigation {
    override fun getHome(): NavDirections =
        SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()

    override fun getSignInFragment(): NavDirections =
        SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
}