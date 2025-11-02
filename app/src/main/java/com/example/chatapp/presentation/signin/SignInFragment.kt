package com.example.chatapp.presentation.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignInBinding
import com.example.chatapp.domain.enums.ErrorInputPasswordEnum
import com.example.chatapp.presentation.signin.nav.navigateToHome
import com.example.chatapp.presentation.signin.nav.navigateToSignUp
import com.example.chatapp.presentation.signin.state.SignInEvent
import com.example.chatapp.presentation.signin.state.SignInState
import com.example.chatapp.presentation.viewutils.togglePasswordVisibility
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStateObserver()
        setupEventObserver()
        setupListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupStateObserver() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignInState.Logged -> {
                    clearState()
                    navigateToHome()
                }
                else -> Unit
            }
        }
    }

    private fun setupEventObserver() = with(binding) {
        viewModel.event.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignInEvent.Loading -> binding.btnSignIn.setLoadingButton(true)
                is SignInEvent.InvalidEmail -> inputEmail.error =
                    getText(R.string.txt_invalid_email)
                is SignInEvent.EmptyEmail -> inputEmail.error =
                    getText(R.string.txt_empty_email)

                is SignInEvent.InvalidPassword ->
                    setPasswordError(ErrorInputPasswordEnum.INVALID_INPUT)

                is SignInEvent.EmptyPassword -> setPasswordError(ErrorInputPasswordEnum.EMPTY_INPUT)
                is SignInEvent.PasswordIsVisible -> inputPassword.togglePasswordVisibility(
                    layout = inputPasswordLayout,
                    isVisible = state.isVisible
                )

                is SignInEvent.IsValidForm -> isValidForm()
                is SignInEvent.SignInError -> {
                    clearState()

                    setAlertView(
                        visibility = true,
                        message = state.error
                    )
                }
            }
        }
    }

    private fun setupListeners() = with(binding) {
        inputPasswordLayout.setEndIconOnClickListener {
            viewModel.togglePasswordVisibility()
        }
        txtLink.setOnClickListener {
            navigateToSignUp()
        }
        btnSignIn.setOnClickListener {
            validateForm()
        }
    }

    private fun validateForm() = with(binding) {
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        viewModel.validateForm(
            email = email,
            password = password
        )
    }

    private fun isValidForm() = with(binding) {
        clearState()

        inputPasswordLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM

        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        viewModel.signIn(
            email = email,
            password = password
        )
    }

    private fun setPasswordError(type: ErrorInputPasswordEnum) = with(binding) {
        inputPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE

        inputEmail.error = when (type) {
            ErrorInputPasswordEnum.EMPTY_INPUT -> getText(R.string.txt_empty_password)
            ErrorInputPasswordEnum.INVALID_INPUT -> getText(R.string.txt_invalid_password)
        }
    }

    private fun setAlertView(
        visibility: Boolean,
        message: String = String()
    ) = with(binding.componentAlert) {
        containerAlert.visibility = if (visibility) View.VISIBLE else View.GONE
        textAlert.text = message
    }

    private fun clearState() {
        binding.btnSignIn.setLoadingButton(false)
        setAlertView(visibility = false)
    }
}