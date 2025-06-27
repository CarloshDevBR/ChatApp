package com.example.chatapp.core.navigation

import androidx.navigation.NavDirections
import com.example.chatapp.presentation.auth.signin.SignInFragmentDirections
import com.example.chatapp.presentation.auth.signup.SignUpFragmentDirections
import com.example.chatapp.presentation.home.HomeFragmentDirections

class ChatNavigationImpl : ChatNavigation {
    override fun getHomeFromSignInFragment(): NavDirections =
        SignInFragmentDirections.actionSignInFragmentToHomeFragment()

    override fun getSignUpFromSignInFragment(): NavDirections =
        SignInFragmentDirections.actionSignInFragmentToSignUpFragment()

    override fun getHomeFromSignUpFragment(): NavDirections =
        SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()

    override fun getSignInFromSignUpFragment(): NavDirections =
        SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()

    override fun getSignInFromHomeFragment(): NavDirections =
        HomeFragmentDirections.actionHomeFragmentToSignInFragment()
}