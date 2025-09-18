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
        setupListeners()
        setupStateObserver()
        setupEventObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
                is SignUpEvent.Loading -> setLoadingButton(true)
                is SignUpEvent.InvalidName -> inputName.error = getText(R.string.txt_empty)
                is SignUpEvent.InvalidEmail -> inputEmail.error =
                    getText(R.string.txt_invalid_email)
                is SignUpEvent.EmptyEmail -> inputEmail.error = getText(R.string.txt_invalid_email)
                is SignUpEvent.InvalidPassword -> invalidPassword()
                is SignUpEvent.EmptyPassword -> emptyPassword()
                is SignUpEvent.IsValidForm -> {
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
                is SignUpEvent.PasswordVisible -> togglePasswordVisibility(state.visible)
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

    private fun setLoadingButton(value: Boolean) = with(binding.btnSignUp) {
        isLoading = value
    }

    private fun setAlertView(
        visibility: Boolean,
        message: String = ""
    ) = with(binding.componentAlert) {
        containerAlert.visibility = if (visibility) View.VISIBLE else View.GONE
        textAlert.text = message
    }

    private fun navigateToHome() {
        val nav = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
        findNavController().navigate(nav)
    }

    private fun navigateToSignIn() {
        val nav = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
        findNavController().navigate(nav)
    }

    private fun clearState() {
        setLoadingButton(false)
        setAlertView(visibility = false)
        viewModel.clearState()
    }
}