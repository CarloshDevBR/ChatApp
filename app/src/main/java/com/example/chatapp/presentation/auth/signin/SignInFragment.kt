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
        setupListeners()
        setupStateObserver()
        setupEventObserver()
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
                is SignInEvent.Loading -> setLoadingButton(true)
                is SignInEvent.InvalidEmail -> inputEmail.error =
                    getText(R.string.txt_invalid_email)

                is SignInEvent.EmptyEmail -> inputEmail.error = getText(R.string.txt_empty_email)
                is SignInEvent.InvalidPassword -> invalidPassword()
                is SignInEvent.EmptyPassword -> emptyPassword()
                is SignInEvent.PasswordVisible -> togglePasswordVisibility(state.visible)
                is SignInEvent.IsValidForm -> {
                    clearState()

                    inputPasswordLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM

                    val email = inputEmail.text.toString().trim()
                    val password = inputPassword.text.toString().trim()

                    viewModel.signIn(
                        email = email,
                        password = password
                    )
                }

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

    private fun validateForm() = with(binding) {
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

    private fun setLoadingButton(value: Boolean) = with(binding.btnSignIn) {
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
        val nav = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
        findNavController().navigate(nav)
    }

    private fun navigateToSignUp() {
        val nav = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(nav)
    }

    private fun clearState() {
        setLoadingButton(false)
        setAlertView(visibility = false)
        viewModel.clearState()
    }
}