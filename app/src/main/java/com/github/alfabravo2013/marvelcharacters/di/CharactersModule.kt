package com.github.alfabravo2013.marvelcharacters.di

import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersLocalDataSource
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersRemoteDataSource
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersRepository
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersUseCase
import com.github.alfabravo2013.marvelcharacters.presentation.characters.CharactersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersModule = module {
    single { CharactersRemoteDataSource(get()) }
    single { CharactersLocalDataSource() }
    single { CharactersRepository(get(), get()) }
    factory { CharactersUseCase(get()) }
    viewModel { CharactersViewModel(get()) }
}
