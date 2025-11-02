package com.example.chatapp.presentation.signup.nav

import androidx.navigation.fragment.findNavController
import com.example.chatapp.presentation.signup.SignUpFragment
import com.example.chatapp.presentation.signup.SignUpFragmentDirections

fun SignUpFragment.navigateToHome() {
    val nav = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
    findNavController().navigate(nav)
}

fun SignUpFragment.navigateToSignIn() {
    val nav = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
    findNavController().navigate(nav)
}