package com.example.chatapp.presentation.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.data.errors.AuthError
import com.example.chatapp.domain.business.SignInBusiness
import com.example.chatapp.domain.usecase.auth.SignInUseCase
import com.example.chatapp.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.chatapp.presentation.auth.signin.SignInState as State

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val signInBusiness: SignInBusiness
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val _isPasswordVisible = MutableLiveData(false)
    val isPasswordVisible: LiveData<Boolean> get() = _isPasswordVisible

    fun validateForm(email: String, password: String) {
        _state.value = signInBusiness.isValidForm(email, password)
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase.invoke(
                params = SignInUseCase.Params(
                    email = email,
                    password = password
                )
            ).onStart {
                _state.value = State.Loading
            }.catch {
                handlerError(it as AuthError)
            }.collect {
                _state.value = State.Logged
                saveUser(it)
            }
        }
    }

    private fun saveUser(user: UserResponse) {
        viewModelScope.launch {
            saveUserUseCase.invoke(
                params = SaveUserUseCase.Params(user)
            )
        }
    }

    private fun handlerError(error: AuthError) {
        _state.value = State.SignInError(
            when (error) {
                AuthError.InvalidCredentials -> INVALID_CREDENTIALS
                AuthError.UserNotFound -> USER_NOT_FOUND
                else -> "$UNKNOWN_ERROR ${error.message}"
            }
        )
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value!!
    }

    fun clearState() {
        _state.value = State.InitialState
    }

    private companion object {
        const val INVALID_CREDENTIALS = "Credenciais inválidas"
        const val USER_NOT_FOUND = "Usuário não encontrado"
        const val UNKNOWN_ERROR = "Erro desconhecido: "
    }
}