package com.example.chatapp.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.core.livedata.single.SingleLiveEvent
import com.example.chatapp.core.resourceprovider.ResourceProvider
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.business.SignUpBusiness
import com.example.chatapp.domain.errors.AuthError
import com.example.chatapp.domain.usecase.auth.SignUpUseCase
import com.example.chatapp.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.chatapp.presentation.signup.state.SignUpEvent as Event
import com.example.chatapp.presentation.signup.state.SignUpState as State

class SignUpViewModel(
    private val resourceProvider: ResourceProvider,
    private val signUpUseCase: SignUpUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val signUpBusiness: SignUpBusiness
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val _event = SingleLiveEvent<Event>()
    val event: LiveData<Event> get() = _event

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            signUpUseCase.invoke(
                params = SignUpUseCase.Params(
                    name = name,
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
                _state.value = State.Registered
            }
        }
    }

    private fun handlerError(error: AuthError) {
        _event.value = Event.SignUpError(
            when (error) {
                is AuthError.EmailAlreadyInUse -> resourceProvider.getString(R.string.txt_email_already_in_use)
                is AuthError.NetworkError -> resourceProvider.getString(R.string.txt_network_error)
                else -> resourceProvider.getString(
                    R.string.txt_unknown_error,
                    error.message.toString()
                )
            }
        )
    }

    fun validateForm(name: String, email: String, password: String) {
        _event.value = signUpBusiness.isValidateForm(name, email, password)
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