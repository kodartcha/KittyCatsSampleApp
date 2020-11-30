package com.gabriel_codarcea.core.data.di

import com.gabriel_codarcea.core.data.database.BreedsDatabase
import com.gabriel_codarcea.core.data.repository.BreedsDatabaseRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { BreedsDatabase.getDatabase(androidContext()) }
    single { BreedsDatabaseRepository(get()) }
}
