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
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
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

    private fun navigateToSignIn() {
        val navDirections = HomeFragmentDirections.actionHomeFragmentToSignInFragment()
        findNavController().navigate(navDirections)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }
}