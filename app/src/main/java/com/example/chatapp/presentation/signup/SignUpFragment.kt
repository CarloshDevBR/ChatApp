package com.example.chatapp.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignUpBinding
import com.example.chatapp.domain.enums.ErrorInputPasswordEnum
import com.example.chatapp.presentation.signup.nav.navigateToHome
import com.example.chatapp.presentation.signup.nav.navigateToSignIn
import com.example.chatapp.presentation.signup.state.SignUpEvent
import com.example.chatapp.presentation.signup.state.SignUpState
import com.example.chatapp.presentation.viewutils.togglePasswordVisibility
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(
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
                is SignUpState.Registered -> {
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
                is SignUpEvent.Loading -> binding.btnSignUp.setLoadingButton(true)
                is SignUpEvent.InvalidName -> inputName.error = getText(R.string.txt_empty)
                is SignUpEvent.InvalidEmail -> inputEmail.error =
                    getText(R.string.txt_invalid_email)
                is SignUpEvent.EmptyEmail -> inputEmail.error =
                    getText(R.string.txt_invalid_email)
                is SignUpEvent.InvalidPassword ->
                    setPasswordError(ErrorInputPasswordEnum.INVALID_INPUT)
                is SignUpEvent.EmptyPassword -> setPasswordError(ErrorInputPasswordEnum.EMPTY_INPUT)
                is SignUpEvent.IsValidForm -> isValidForm()
                is SignUpEvent.PasswordIsVisible -> inputPassword.togglePasswordVisibility(
                    layout = inputPasswordLayout,
                    isVisible = state.isVisible
                )
                is SignUpEvent.SignUpError -> {
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
            navigateToSignIn()
        }
        btnSignUp.setOnClickListener {
            validateForm()
        }
    }

    private fun validateForm() = with(binding) {
        val name = inputName.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        viewModel.validateForm(
            name = name,
            email = email,
            password = password
        )
    }

    private fun isValidForm() = with(binding) {
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
        binding.btnSignUp.setLoadingButton(false)
        setAlertView(visibility = false)
    }
}