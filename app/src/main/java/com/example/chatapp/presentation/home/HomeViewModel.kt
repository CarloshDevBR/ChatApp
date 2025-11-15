package com.example.chatapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.core.livedata.single.SingleLiveEvent
import com.example.chatapp.core.resourceprovider.ResourceProvider
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.domain.usecase.user.GetUserUseCase
import com.example.chatapp.domain.usecase.user.LogoutUserUseCase
import kotlinx.coroutines.launch
import com.example.chatapp.presentation.home.state.HomeEvent as Event
import com.example.chatapp.presentation.home.state.HomeState as State

class HomeViewModel(
    private val resourceProvider: ResourceProvider,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    private val _event = SingleLiveEvent<Event>()
    val event: LiveData<Event> get() = _event

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    init {
        setupTabsScreen()
    }

    private fun setupTabsScreen() = with(resourceProvider) {
        _state.value = State.Tabs(
            tabs = mapOf(
                POSITION_CONVERSATION to getString(R.string.tab_conversations),
                POSITION_CONTACTS to getString(R.string.tab_contacts)
            )
        )
    }

    fun getUser() {
        viewModelScope.launch {
            getUserUseCase.invoke()
                .collect {
                    if (it != null) {
                        _user.value = it
                        return@collect
                    }
                    _state.value = State.LoggedOut
                }
        }
    }

    fun navigateToProfile() {
        _event.value = Event.NavigateToProfile
    }

    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase.invoke()
                .collect {
                    _state.value = State.LoggedOut
                }
        }
    }

    fun getWelcomeMessage(name: String) =
        resourceProvider.getString(R.string.txt_title_toolbar, name)

    private companion object {
        const val POSITION_CONVERSATION = 0
        const val POSITION_CONTACTS = 1
    }
}