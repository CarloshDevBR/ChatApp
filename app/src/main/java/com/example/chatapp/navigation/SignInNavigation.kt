package com.example.chatapp.navigation

import androidx.navigation.NavDirections

interface SignInNavigation {
    fun getHomeFragment(): NavDirections

    fun getSignUpFragment(): NavDirections
}