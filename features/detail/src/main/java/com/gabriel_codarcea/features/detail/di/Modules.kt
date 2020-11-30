package com.gabriel_codarcea.features.detail.di

import com.gabriel_codarcea.features.detail.viewmodel.BreedDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { BreedDetailViewModel() }
}