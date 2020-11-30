package com.gabriel_codarcea.core.network.di

import com.gabriel_codarcea.core.network.client.ApiClient
import com.gabriel_codarcea.core.network.client.LoginClient
import com.gabriel_codarcea.core.network.manager.BreedsManager
import com.gabriel_codarcea.core.network.manager.LoginManager
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient }
    single { LoginClient }
    single { BreedsManager() }
    single { LoginManager() }
}