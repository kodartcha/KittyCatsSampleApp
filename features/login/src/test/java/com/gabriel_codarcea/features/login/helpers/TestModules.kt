package com.gabriel_codarcea.features.login.helpers

import com.gabriel_codarcea.core.data.preferences.SharedPreferencesManager
import com.gabriel_codarcea.core.network.manager.LoginManager
import io.mockk.mockk
import org.koin.dsl.module

val testModule = module {
    single { mockk<SharedPreferencesManager>() }
}