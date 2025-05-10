package com.example.chatapp.presentation.auth.signup

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignUpBinding
import com.example.chatapp.navigation.SignUpNavigation
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {
    private val binding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater)
    }
    private val viewModel: SignUpViewModel by viewModel()
    private val navigation by inject<SignUpNavigation>()

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
            navigateToSignIn()
        }
        btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun setupObservers() = with(binding) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignUpState.InitialState -> {}
                SignUpState.Subscribed -> {
                    clearState()
                    navigateToHome()
                }

                SignUpState.Loading -> setLoadingButton(true)
                SignUpState.InvalidName -> {
                    inputName.error = getText(R.string.txt_empty)
                }

                SignUpState.InvalidEmail -> {
                    inputEmail.error = getText(R.string.txt_invalid_email)
                }

                SignUpState.EmptyEmail -> {
                    inputEmail.error = getText(R.string.txt_invalid_email)
                }

                SignUpState.InvalidPassword -> invalidPassword()
                SignUpState.EmptyPassword -> emptyPassword()

                is SignUpState.SignUpError -> {
                    clearState()

                    setAlertView(
                        visibility = true,
                        message = state.error
                    )
                }

                SignUpState.IsValidForm -> {
                    clearState()

                    inputPasswordLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM

                    val name = inputName.text.toString()
                    val email = inputEmail.text.toString()
                    val password = inputPassword.text.toString()

                    viewModel.signUp(
                        name = name,
                        email = email,
                        password = password,
                    )
                }
            }
        }
        viewModel.isPasswordVisible.observe(viewLifecycleOwner) { state ->
            togglePasswordVisibility(visibility = state)
        }
    }

    private fun setLoadingButton(value: Boolean) = with(binding) {
        btnSignUp.isLoading = value
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

    private fun signUp() = with(binding) {
        val name = inputName.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        viewModel.validateForm(
            name = name,
            email = email,
            password = password
        )
    }

    private fun navigateToHome() {
        val action = navigation.getHome()
        findNavController().navigate(action)
    }

    private fun navigateToSignIn() {
        val action = navigation.getSignInFragment()
        findNavController().navigate(action)
    }

    private fun setAlertView(
        visibility: Boolean,
        message: String = ""
    ) = with(binding) {
        componentAlert.containerAlert.visibility = if (visibility) View.VISIBLE else View.GONE
        componentAlert.textAlert.text = message
    }

    private fun clearState() {
        setLoadingButton(false)
        setAlertView(visibility = false)
        viewModel.clearState()
    }
}