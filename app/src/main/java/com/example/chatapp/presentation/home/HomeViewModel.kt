package com.example.chatapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.core.domain.response.UserResponse
import com.example.chatapp.domain.usecase.user.GetUserUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _state = MutableLiveData<UserResponse>()
    val state: LiveData<UserResponse> get() = _state

    fun getUser() {
        viewModelScope.launch {
            getUserUseCase.invoke()
                .collect {
                    _state.value = it
                }
        }
    }
}