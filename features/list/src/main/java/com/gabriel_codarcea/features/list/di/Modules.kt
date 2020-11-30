package com.gabriel_codarcea.features.list.di

import com.gabriel_codarcea.features.list.viewmodel.BreedsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listModule = module {
    viewModel { BreedsListViewModel() }
}
