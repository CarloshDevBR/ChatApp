package com.example.chatapp.presentation.auth.signin

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignInBinding
import com.example.chatapp.navigation.SignInNavigation
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {
    private val binding by lazy {
        FragmentSignInBinding.inflate(layoutInflater)
    }
    private val viewModel: SignInViewModel by viewModel()
    private val navigation by inject<SignInNavigation>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() = with(binding) {
        inputPasswordLayout.setEndIconOnClickListener {
            viewModel.togglePasswordVisibility()
        }
        txtLink.setOnClickListener {
            navigateToSignUp()
        }
        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun setupObservers() = with(binding) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignInState.InitialState -> {}
                SignInState.Logged -> {
                    clearState()
                    navigateToHome()
                }

                SignInState.Loading -> setLoadingButton(true)
                SignInState.InvalidEmail -> {
                    inputEmail.error = getText(R.string.txt_invalid_email)
                }

                SignInState.EmptyEmail -> {
                    inputEmail.error = getText(R.string.txt_empty_email)
                }

                SignInState.InvalidPassword -> invalidPassword()
                SignInState.EmptyPassword -> emptyPassword()

                is SignInState.SignInError -> {
                    clearState()

                    setAlertView(
                        visibility = true,
                        message = state.error
                    )
                }

                SignInState.IsValidForm -> {
                    clearState()

                    inputPasswordLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM

                    val email = inputEmail.text.toString().trim()
                    val password = inputPassword.text.toString().trim()

                    viewModel.signIn(
                        email = email,
                        password = password
                    )
                }
            }
        }
        viewModel.isPasswordVisible.observe(viewLifecycleOwner) { state ->
            togglePasswordVisibility(visibility = state)
        }
    }

    private fun signIn() = with(binding) {
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        viewModel.validateForm(
            email = email,
            password = password
        )
    }

    private fun togglePasswordVisibility(visibility: Boolean) = with(binding) {
        if (visibility) {
            inputPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            inputPasswordLayout.endIconDrawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_visibility_off
            )
        } else {
            inputPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            inputPasswordLayout.endIconDrawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_visibility
            )
        }

        inputPassword.setSelection(inputPassword.text?.length ?: 0)
    }

    private fun invalidPassword() = with(binding) {
        inputPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE
        inputPassword.error = getText(R.string.txt_invalid_password)
    }

    private fun emptyPassword() = with(binding) {
        inputPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE
        inputEmail.error = getText(R.string.txt_empty_password)
    }

    private fun setLoadingButton(value: Boolean) = with(binding) {
        btnSignIn.isLoading = value
    }

    private fun setAlertView(
        visibility: Boolean,
        message: String = ""
    ) = with(binding) {
        componentAlert.containerAlert.visibility = if (visibility) View.VISIBLE else View.GONE
        componentAlert.textAlert.text = message
    }

    private fun navigateToHome() {
        val action = navigation.getHomeFragment()
        findNavController().navigate(action)
    }

    private fun navigateToSignUp() {
        val action = navigation.getSignUpFragment()
        findNavController().navigate(action)
    }

    private fun clearState() {
        setLoadingButton(false)
        setAlertView(visibility = false)
        viewModel.clearState()
    }
}