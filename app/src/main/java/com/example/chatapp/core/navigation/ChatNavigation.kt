package com.example.chatapp.core.navigation

import androidx.navigation.NavDirections

interface ChatNavigation {
    fun getHomeFromSignInFragment(): NavDirections
    fun getSignUpFromSignInFragment(): NavDirections
    fun getHomeFromSignUpFragment(): NavDirections
    fun getSignInFromSignUpFragment(): NavDirections
}