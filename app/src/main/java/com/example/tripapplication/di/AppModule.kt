package com.example.tripapplication.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.tripapplication.auth.data.local.AuthDataStore
import com.example.tripapplication.auth.data.local.KeycloakConfig
import com.example.tripapplication.auth.data.service.AuthServiceImpl
import com.example.tripapplication.auth.domain.AuthService
import com.example.tripapplication.auth.presentation.auth_screen.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Remote
    single { KeycloakConfig(androidContext()) }
    single { AuthDataStore(get()) }
    single { get<KeycloakConfig>().getAuthService() }

    // Services
    single<AuthService> { AuthServiceImpl(get(), get(), get(), get()) }

    // ViewModel
    viewModel { AuthViewModel(get(), get()) }
}