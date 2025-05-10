package com.example.chatapp.navigation

import androidx.navigation.NavDirections

interface SignUpNavigation {
    fun getHome(): NavDirections

    fun getSignInFragment(): NavDirections
}