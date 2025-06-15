package com.example.chatapp.di

import com.example.chatapp.MainViewModel
import com.example.chatapp.data.datasource.SharedPreferencesDataSourceImpl
import com.example.chatapp.data.repository.FireStoreAuthRepositoryImpl
import com.example.chatapp.data.repository.FirebaseAuthRepositoryImpl
import com.example.chatapp.data.repository.UserPreferencesRepositoryImpl
import com.example.chatapp.domain.business.SignInBusiness
import com.example.chatapp.domain.business.SignInBusinessImpl
import com.example.chatapp.domain.business.SignUpBusiness
import com.example.chatapp.domain.business.SignUpBusinessImpl
import com.example.chatapp.domain.datasource.SharedPreferencesDataSource
import com.example.chatapp.domain.repository.FireStoreAuthRepository
import com.example.chatapp.domain.repository.FirebaseAuthRepository
import com.example.chatapp.domain.repository.UserPreferencesRepository
import com.example.chatapp.domain.usecase.auth.SignInUseCase
import com.example.chatapp.domain.usecase.auth.SignInUseCaseImpl
import com.example.chatapp.domain.usecase.auth.SignOutUseCase
import com.example.chatapp.domain.usecase.auth.SignOutUseCaseImpl
import com.example.chatapp.domain.usecase.auth.SignUpUseCase
import com.example.chatapp.domain.usecase.auth.SignUpUseCaseImpl
import com.example.chatapp.domain.usecase.user.GetUserUseCase
import com.example.chatapp.domain.usecase.user.GetUserUseCaseImpl
import com.example.chatapp.domain.usecase.user.SaveUserUseCase
import com.example.chatapp.domain.usecase.user.SaveUserUseCaseImpl
import com.example.chatapp.navigation.ChatNavigation
import com.example.chatapp.navigation.ChatNavigationImpl
import com.example.chatapp.presentation.auth.signin.SignInViewModel
import com.example.chatapp.presentation.auth.signup.SignUpViewModel
import com.example.chatapp.presentation.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class ApplicationModules {
    fun load() = listOf(
        module {
            factoryAuth()
            factoryDataSource()
            factoryViewModel()
            factoryBusiness()
            factoryUseCase()
            factoryNavigation()
        }
    )

    private fun Module.factoryAuth() {
        single { FirebaseAuth.getInstance() }
        single { FirebaseFirestore.getInstance() }
        single<UserPreferencesRepository> {
            UserPreferencesRepositoryImpl(
                dataSource = get()
            )
        }
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
    }

    private fun Module.factoryDataSource() {
        single<SharedPreferencesDataSource> {
            SharedPreferencesDataSourceImpl(
                context = get()
            )
        }
    }

    private fun Module.factoryViewModel() {
        viewModel {
            MainViewModel(
                getUserUseCase = get()
            )
        }
        viewModel {
            SignUpViewModel(
                signUpUseCase = get(),
                saveUserUseCase = get(),
                signUpBusiness = get()
            )
        }
        viewModel {
            SignInViewModel(
                signInUseCase = get(),
                saveUserUseCase = get(),
                signInBusiness = get()
            )
        }
        viewModel {
            HomeViewModel(
                getUserUseCase = get()
            )
        }
    }

    private fun Module.factoryBusiness() {
        factory<SignInBusiness> {
            SignInBusinessImpl()
        }
        factory<SignUpBusiness> {
            SignUpBusinessImpl()
        }
    }

    private fun Module.factoryUseCase() {
        single<GetUserUseCase> {
            GetUserUseCaseImpl(
                repository = get()
            )
        }
        single<SaveUserUseCase> {
            SaveUserUseCaseImpl(
                repository = get()
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

    private fun Module.factoryNavigation() {
        factory<ChatNavigation> {
            ChatNavigationImpl()
        }
    }
}