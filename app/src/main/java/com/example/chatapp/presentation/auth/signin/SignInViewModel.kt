package com.example.chatapp.presentation.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.core.livedata.single.SingleLiveEvent
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.business.SignInBusiness
import com.example.chatapp.domain.errors.AuthError
import com.example.chatapp.domain.usecase.auth.SignInUseCase
import com.example.chatapp.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.chatapp.presentation.auth.signin.SignInEvent as Event
import com.example.chatapp.presentation.auth.signin.SignInState as State

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val signInBusiness: SignInBusiness
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val _event = SingleLiveEvent<Event>()
    val event: LiveData<Event> get() = _event

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase.invoke(
                params = SignInUseCase.Params(
                    email = email,
                    password = password
                )
            ).onStart {
                _event.value = Event.Loading
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
                _state.value = State.Logged
            }
        }
    }

    private fun handlerError(error: AuthError) {
        _event.value = Event.SignInError(
            when (error) {
                AuthError.InvalidCredentials -> INVALID_CREDENTIALS
                else -> "$UNKNOWN_ERROR ${error.message}"
            }
        )
    }

    fun validateForm(email: String, password: String) {
        _event.value = signInBusiness.isValidForm(email, password)
    }

    fun togglePasswordVisibility() {
        val state = _event.value
        if (state is Event.PasswordVisible) {
            _event.value = Event.PasswordVisible(state.visible)
        }
    }

    fun clearState() {
        _state.value = State.InitialState
    }

    private companion object {
        const val INVALID_CREDENTIALS = "Credenciais inválidas"
        const val UNKNOWN_ERROR = "Erro desconhecido: "
    }
}