package com.example.chatapp.presentation.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.business.SignUpBusiness
import com.example.chatapp.domain.errors.AuthError
import com.example.chatapp.domain.usecase.auth.SignUpUseCase
import com.example.chatapp.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.chatapp.presentation.auth.signup.SignUpState as State

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val signUpBusiness: SignUpBusiness
) : ViewModel() {
    private val _state = MutableLiveData<State?>()
    val state: LiveData<State?> get() = _state

    private val _isPasswordVisible = MutableLiveData(false)
    val isPasswordVisible: LiveData<Boolean> get() = _isPasswordVisible

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            signUpUseCase.invoke(
                params = SignUpUseCase.Params(
                    name = name,
                    email = email,
                    password = password
                )
            ).onStart {
                _state.value = State.Loading
            }.catch {
                handlerError(it as AuthError)
            }.collect {
                saveUser(it)
            }
        }
    }

    private fun saveUser(user: UserResponse) {
        viewModelScope.launch {
            saveUserUseCase.invoke(
                params = SaveUserUseCase.Params(user)
            ).collect {
                _state.value = State.Subscribed
            }
        }
    }

    private fun handlerError(error: AuthError) {
        _state.value = State.SignUpError(
            when (error) {
                AuthError.EmailAlreadyInUse -> EMAIL_ALREADY_IN_USE
                AuthError.NetworkError -> NETWORK_ERROR
                else -> "$UNKNOWN_ERROR ${error.message}"
            }
        )
    }

    fun validateForm(name: String, email: String, password: String) {
        _state.value = signUpBusiness.isValidateForm(name, email, password)
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value!!
    }

    fun clearState() {
        _state.value = null
    }

    private companion object {
        const val EMAIL_ALREADY_IN_USE = "O E-mail já existe"
        const val NETWORK_ERROR = "Ocorreu um erro com a conexão"
        const val UNKNOWN_ERROR = "Erro desconhecido: "
    }
}