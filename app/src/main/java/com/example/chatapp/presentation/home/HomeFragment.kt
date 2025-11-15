package com.example.chatapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.R
import com.example.chatapp.data.model.response.UserResponse
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.presentation.home.adapter.TabsHomeAdapter
import com.example.chatapp.presentation.home.nav.navigateToProfile
import com.example.chatapp.presentation.home.nav.navigateToSignIn
import com.example.chatapp.presentation.home.state.HomeEvent
import com.example.chatapp.presentation.home.state.HomeState
import com.google.android.material.tabs.TabLayoutMediator
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
        super.onDestroyView()
        _binding = null
    }

    private fun setupStateObserver() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Tabs -> setupTabs(state.tabs)
                is HomeState.LoggedOut -> navigateToSignIn()
            }
        }
        viewModel.user.observe(viewLifecycleOwner) { state ->
            setupToolbar(state)
        }
    }

    private fun setupEventObserver() {
        viewModel.event.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeEvent.NavigateToProfile -> navigateToProfile()
            }
        }
    }

    private fun setupListeners() = with(binding) {
        includeTabsHome.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                // TODO - remover
                delay(1000)
                binding.includeTabsHome.swipeRefreshLayout.isRefreshing = IS_REFRESHING
            }
        }
    }

    private fun setupTabs(tabs: Map<Int, String>) = with(binding.includeTabsHome) {
        val adapter = TabsHomeAdapter(childFragmentManager, lifecycle)

        viewPager2.adapter = adapter
        viewPager2.isUserInputEnabled = IS_USER_INPUT_ENABLED

        TabLayoutMediator(
            tabLayout,
            viewPager2
        ) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun setupToolbar(user: UserResponse) = with(binding.toolbarHome) {
        title = viewModel.getWelcomeMessage(user.name)
        inflateMenu(R.menu.menu_item)
        setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    viewModel.navigateToProfile()
                    MENU_CLICK_DEFAULT
                }
                R.id.logout -> {
                    viewModel.logout()
                    MENU_CLICK_DEFAULT
                }

                else -> MENU_CLICK_DEFAULT
            }
        }
    }

    private companion object {
        const val IS_USER_INPUT_ENABLED = false
        const val IS_REFRESHING = false
        const val MENU_CLICK_DEFAULT = true
    }
}