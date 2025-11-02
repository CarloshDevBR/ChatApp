package com.example.chatapp.presentation.home.nav

import androidx.navigation.fragment.findNavController
import com.example.chatapp.presentation.home.HomeFragment
import com.example.chatapp.presentation.home.HomeFragmentDirections

fun HomeFragment.navigateToProfile() {
    val navDirections = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
    findNavController().navigate(navDirections)
}

fun HomeFragment.navigateToSignIn() {
    val navDirections = HomeFragmentDirections.actionHomeFragmentToSignInFragment()
    findNavController().navigate(navDirections)
}