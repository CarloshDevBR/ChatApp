package com.example.chatapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.core.base.BaseFragment
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.databinding.FragmentSignInBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModel()

    override fun inflate(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun setupViews() {
        super.setupViews()
        setupListeners()
        viewModel.getUser()
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
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

    private fun setupToolbar(user: UserResponse) = with(binding) {
        toolbarHome.setTitle(viewModel.getWelcomeMessage(user.name))
    }
}