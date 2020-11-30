package com.gabriel_codarcea.features.login.di

import com.gabriel_codarcea.features.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel { LoginViewModel() }
}
