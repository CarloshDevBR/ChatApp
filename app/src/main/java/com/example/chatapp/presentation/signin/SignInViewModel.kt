package com.example.chatapp.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.core.livedata.single.SingleLiveEvent
import com.example.chatapp.core.resourceprovider.ResourceProvider
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.business.SignInBusiness
import com.example.chatapp.domain.errors.AuthError
import com.example.chatapp.domain.usecase.auth.SignInUseCase
import com.example.chatapp.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.chatapp.presentation.signin.state.SignInEvent as Event
import com.example.chatapp.presentation.signin.state.SignInState as State

class SignInViewModel(
    private val resourceProvider: ResourceProvider,
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
                is AuthError.InvalidCredentials -> resourceProvider.getString(R.string.txt_invalid_credentials)
                else -> resourceProvider.getString(
                    R.string.txt_unknown_error,
                    error.message.toString()
                )
            }
        )
    }

    fun validateForm(email: String, password: String) {
        _event.value = signInBusiness.isValidForm(email, password)
    }

    fun togglePasswordVisibility() {
        val state = event.value
        if (state !is Event.PasswordIsVisible) {
            _event.value = Event.PasswordIsVisible(true)
            return
        }
        _event.value = Event.PasswordIsVisible(state.isVisible.not())
    }
}