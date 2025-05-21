package com.ciputramitra.di

import com.ciputramitra.cache.DataStoreManager
import com.ciputramitra.consultation.ui.auth.AuthViewModel
import com.ciputramitra.domain.repository.AuthRepository
import com.ciputramitra.domain.repository.AuthRepositoryImpl
import com.ciputramitra.domain.usecase.AuthUseCase
import com.ciputramitra.remote.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
	single { DataStoreManager(androidContext()) }
	single { HttpClient(get()) }
	single { get<HttpClient>().getApi() ?: throw IllegalStateException("API not initialized") }
	
	//    Authentication
	single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
	factory { AuthUseCase(get()) }
	viewModel { AuthViewModel(get(), get()) }
}