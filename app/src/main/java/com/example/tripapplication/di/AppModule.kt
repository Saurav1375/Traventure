package com.example.tripapplication.di

import com.example.tripapplication.auth.data.local.AuthDataStore
import com.example.tripapplication.auth.data.networking.AuthServiceImpl
import com.example.tripapplication.auth.domain.AuthService
import com.example.tripapplication.auth.presentation.login.AuthViewModel
import com.example.tripapplication.core.data.networking.HttpClientFactory
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(engine = CIO.create()) }

    // Remote
    single { AuthDataStore(get()) }

    // Services
    single<AuthService> { AuthServiceImpl(get(), get()) }

    // ViewModel
    viewModel { AuthViewModel(get()) }
}