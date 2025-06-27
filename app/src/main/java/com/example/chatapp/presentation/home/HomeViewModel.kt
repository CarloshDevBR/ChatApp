package com.example.chatapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.core.resourceprovider.ResourceProvider
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.usecase.user.GetUserUseCase
import com.example.chatapp.domain.usecase.user.LogoutUserUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val resourceProvider: ResourceProvider,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    fun getUser() {
        viewModelScope.launch {
            getUserUseCase.invoke()
                .collect {
                    _user.value = it
                }
        }
    }

    fun getWelcomeMessage(name: String) =
        resourceProvider.getString(R.string.txt_title_toolbar, name)

    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase.invoke()
        }
    }
}