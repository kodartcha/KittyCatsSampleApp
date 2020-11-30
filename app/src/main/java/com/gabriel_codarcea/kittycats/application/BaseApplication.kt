package com.gabriel_codarcea.kittycats.application

import android.app.Application
import com.gabriel_codarcea.kittycats.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(appComponent)
        }
    }
}
