package com.example.chatapp.di

import com.example.chatapp.data.repository.FireStoreAuthRepositoryImpl
import com.example.chatapp.data.repository.FirebaseAuthRepositoryImpl
import com.example.chatapp.domain.repository.FireStoreAuthRepository
import com.example.chatapp.domain.repository.FirebaseAuthRepository
import com.example.chatapp.domain.usecase.auth.SignInUseCase
import com.example.chatapp.domain.usecase.auth.SignInUseCaseImpl
import com.example.chatapp.domain.usecase.auth.SignOutUseCase
import com.example.chatapp.domain.usecase.auth.SignOutUseCaseImpl
import com.example.chatapp.domain.usecase.auth.SignUpUseCase
import com.example.chatapp.domain.usecase.auth.SignUpUseCaseImpl
import com.example.chatapp.navigation.SignInNavigation
import com.example.chatapp.navigation.SignInNavigationImpl
import com.example.chatapp.navigation.SignUpNavigation
import com.example.chatapp.navigation.SignUpNavigationImpl
import com.example.chatapp.presentation.auth.signin.SignInViewModel
import com.example.chatapp.presentation.auth.signup.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class ApplicationModules {
    fun load() {
        loadKoinModules(
            module {
                factoryAuth()
                factoryViewModel()
                factoryNavigation()
            }
        )
    }

    private fun Module.factoryAuth() {
        single { FirebaseAuth.getInstance() }

        single { FirebaseFirestore.getInstance() }

        single<FirebaseAuthRepository> {
            FirebaseAuthRepositoryImpl(
                firebaseAuth = get(),
                fireStoreAuthRepository = get()
            )
        }

        single<FireStoreAuthRepository> {
            FireStoreAuthRepositoryImpl(
                firestore = get()
            )
        }

        factory<SignInUseCase> {
            SignInUseCaseImpl(
                repository = get()
            )
        }
        factory<SignUpUseCase> {
            SignUpUseCaseImpl(
                firebaseRepository = get(),
            )
        }
        factory<SignOutUseCase> {
            SignOutUseCaseImpl(
                repository = get()
            )
        }
    }

    private fun Module.factoryViewModel() {
        viewModel {
            SignUpViewModel(
                signUpUseCase = get()
            )
        }

        viewModel {
            SignInViewModel(
                signInUseCase = get()
            )
        }
    }

    private fun Module.factoryNavigation() {
        factory<SignInNavigation> {
            SignInNavigationImpl()
        }

        factory<SignUpNavigation> {
            SignUpNavigationImpl()
        }
    }
}