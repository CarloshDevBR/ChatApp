package com.example.chatapp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.R
import com.example.chatapp.core.base.BaseFragment
import com.example.chatapp.core.navigation.ChatNavigation
import com.example.chatapp.core.util.hideKeyboard
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModel()
    private val navigation by inject<ChatNavigation>()

    override fun inflate(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun setupViews() {
        setupListeners()
        viewModel.getUser()
    }

    private fun setupListeners() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                delay(1000)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { state ->
            setupToolbar(state)
        }
    }

    private fun setupToolbar(user: UserResponse) = with(binding.toolbarHome) {
        updateToolbar(user)
        inflateMenu(R.menu.menu_item)
        setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> true

                R.id.logout -> {
                    viewModel.logout()
                    navigateToSignIn()
                    true
                }

                else -> true
            }
        }
    }

    private fun updateToolbar(user: UserResponse) = with(binding.toolbarHome) {
        title = viewModel.getWelcomeMessage(user.name)
    }

    private fun navigateToSignIn() = navigate(navigation.getSignInFromHomeFragment())

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }
}