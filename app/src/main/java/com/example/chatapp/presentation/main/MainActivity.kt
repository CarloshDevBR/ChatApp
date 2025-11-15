package com.example.chatapp.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observers()
        setupInsets()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }

    private fun observers() {
        viewModel.isLogged.observe(this) { state ->
            if (state) {
                goToHome()
            }
        }
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun goToHome() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.sign_in_fragment, true)
            .build()
        navController.navigate(R.id.home_fragment, null, navOptions)
    }
}