package com.example.chatapp.presentation.auth.signup

import android.text.InputType
import android.view.View
import androidx.core.content.ContextCompat
import com.example.chatapp.R
import com.example.chatapp.core.base.BaseFragment
import com.example.chatapp.core.navigation.ChatNavigation
import com.example.chatapp.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    private val viewModel: SignUpViewModel by viewModel()
    private val navigation by inject<ChatNavigation>()

    override fun inflate(): FragmentSignUpBinding = FragmentSignUpBinding.inflate(layoutInflater)

    override fun setupViews() {
        super.setupViews()
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
                is SignUpState.Subscribed -> {
                    clearState()
                    navigateToHome()
                }
                is SignUpState.Loading -> setLoadingButton(true)
                is SignUpState.InvalidName -> inputName.error = getText(R.string.txt_empty)
                is SignUpState.InvalidEmail -> inputEmail.error =
                    getText(R.string.txt_invalid_email)

                is SignUpState.EmptyEmail -> inputEmail.error = getText(R.string.txt_invalid_email)
                is SignUpState.InvalidPassword -> invalidPassword()
                is SignUpState.EmptyPassword -> emptyPassword()
                is SignUpState.SignUpError -> {
                    clearState()

                    setAlertView(
                        visibility = true,
                        message = state.error
                    )
                }
                is SignUpState.IsValidForm -> {
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
                null -> {}
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

    private fun setAlertView(
        visibility: Boolean,
        message: String = ""
    ) = with(binding) {
        componentAlert.containerAlert.visibility = if (visibility) View.VISIBLE else View.GONE
        componentAlert.textAlert.text = message
    }

    private fun navigateToHome() = navigate(navigation.getHomeFromSignUpFragment())

    private fun navigateToSignIn() = navigate(navigation.getSignInFromSignUpFragment())

    private fun clearState() {
        setLoadingButton(false)
        setAlertView(visibility = false)
        viewModel.clearState()
    }
}