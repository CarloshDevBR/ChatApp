package com.example.chatapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.core.util.hideKeyboard
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupStateObserver()
        setupEventObserver()
        viewModel.getUser()
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    private fun setupListeners() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                delay(1000)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupStateObserver() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.User -> setupToolbar(state.data)
                else -> Unit
            }
        }
    }

    private fun setupEventObserver() {
        viewModel.event.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeEvent.LoggedOut -> navigateToSignIn()
            }
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
                    true
                }
                else -> true
            }
        }
    }

    private fun updateToolbar(user: UserResponse) = with(binding.toolbarHome) {
        title = viewModel.getWelcomeMessage(user.name)
    }

    private fun navigateToSignIn() {
        val navDirections = HomeFragmentDirections.actionHomeFragmentToSignInFragment()
        findNavController().navigate(navDirections)
    }
}