package com.github.alfabravo2013.marvelcharacters.di

import com.github.alfabravo2013.marvelcharacters.domain.CharacterRepository
import com.github.alfabravo2013.marvelcharacters.domain.LocalDataSource
import com.github.alfabravo2013.marvelcharacters.domain.characterlist.LoadMoreCharactersUseCase
import com.github.alfabravo2013.marvelcharacters.presentation.characterlist.CharacterListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocalDataSource(androidContext()) }
    single { CharacterRepository(get()) }
    factory { LoadMoreCharactersUseCase(get()) }
    viewModel { CharacterListViewModel(get()) }
}
