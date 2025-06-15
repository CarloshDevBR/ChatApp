package com.example.chatapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.domain.usecase.user.GetUserUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean> get() = _isLogged

    fun getUser() {
        viewModelScope.launch {
            getUserUseCase.invoke().collect {
                _isLogged.value = it?.id?.isNotBlank()
            }
        }
    }
}