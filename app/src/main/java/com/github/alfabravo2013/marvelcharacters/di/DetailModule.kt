package com.github.alfabravo2013.marvelcharacters.di

import com.github.alfabravo2013.marvelcharacters.domain.detail.DetailLocalDataSource
import com.github.alfabravo2013.marvelcharacters.domain.detail.DetailRemoteDataSource
import com.github.alfabravo2013.marvelcharacters.domain.detail.DetailRepository
import com.github.alfabravo2013.marvelcharacters.domain.detail.DetailUseCase
import com.github.alfabravo2013.marvelcharacters.presentation.detail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    single { DetailRemoteDataSource(get()) }
    single { DetailLocalDataSource() }
    single { DetailRepository(get(), get()) }
    factory { DetailUseCase(get()) }
    viewModel { DetailViewModel(get()) }
}
