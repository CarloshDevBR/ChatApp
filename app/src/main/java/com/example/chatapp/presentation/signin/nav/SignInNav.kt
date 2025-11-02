package com.example.chatapp.presentation.signin.nav

import androidx.navigation.fragment.findNavController
import com.example.chatapp.presentation.signin.SignInFragment
import com.example.chatapp.presentation.signin.SignInFragmentDirections

fun SignInFragment.navigateToHome() {
    val nav = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
    findNavController().navigate(nav)
}

fun SignInFragment.navigateToSignUp() {
    val nav = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
    findNavController().navigate(nav)
}