package com.gabriel_codarcea.kittycats.di

import com.gabriel_codarcea.core.data.preferences.SharedPreferencesManager
import com.gabriel_codarcea.kittycats.viewmodel.LaunchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LaunchViewModel() }
    single { SharedPreferencesManager(androidContext()) }
}
