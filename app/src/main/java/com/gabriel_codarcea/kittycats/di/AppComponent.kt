package com.gabriel_codarcea.kittycats.di

import com.gabriel_codarcea.core.data.di.dataModule
import com.gabriel_codarcea.core.network.di.networkModule
import com.gabriel_codarcea.features.detail.di.detailModule
import com.gabriel_codarcea.features.list.di.listModule
import com.gabriel_codarcea.features.login.di.loginModule

val appComponent = listOf(
    appModule,
    dataModule,
    networkModule,
    listModule,
    loginModule,
    detailModule
)
