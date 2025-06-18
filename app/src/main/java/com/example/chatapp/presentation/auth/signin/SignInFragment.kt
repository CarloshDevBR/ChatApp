package com.example.chatapp.presentation.auth.signin

import android.text.InputType
import android.view.View
import androidx.core.content.ContextCompat
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignInBinding
import com.example.chatapp.core.navigation.ChatNavigation
import com.example.chatapp.core.base.BaseFragment
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    private val viewModel: SignInViewModel by viewModel()
    private val navigation by inject<ChatNavigation>()

    override fun inflate(): FragmentSignInBinding = FragmentSignInBinding.inflate(layoutInflater)

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
            navigateToSignUp()
        }
        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun setupObservers() = with(binding) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignInState.InitialState -> {}
                is SignInState.Logged -> {
                    clearState()
                    navigateToHome()
                }
                is SignInState.Loading -> setLoadingButton(true)
                is SignInState.InvalidEmail -> {
                    inputEmail.error = getText(R.string.txt_invalid_email)
                }
                is SignInState.EmptyEmail -> {
                    inputEmail.error = getText(R.string.txt_empty_email)
                }
                is SignInState.InvalidPassword -> invalidPassword()
                is SignInState.EmptyPassword -> emptyPassword()
                is SignInState.SignInError -> {
                    clearState()

                    setAlertView(
                        visibility = true,
                        message = state.error
                    )
                }
                is SignInState.IsValidForm -> {
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

    private fun navigateToHome() = navigate(navigation.getHomeFromSignInFragment())

    private fun navigateToSignUp() = navigate(navigation.getSignUpFromSignInFragment())

    private fun clearState() {
        setLoadingButton(false)
        setAlertView(visibility = false)
        viewModel.clearState()
    }
}