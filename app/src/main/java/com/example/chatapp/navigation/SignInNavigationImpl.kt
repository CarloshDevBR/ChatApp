package com.example.chatapp.navigation

import androidx.navigation.NavDirections
import com.example.chatapp.presentation.auth.signin.SignInFragmentDirections

class SignInNavigationImpl : SignInNavigation {
    override fun getHomeFragment(): NavDirections =
        SignInFragmentDirections.actionSignInFragmentToHomeFragment()

    override fun getSignUpFragment(): NavDirections =
        SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
}