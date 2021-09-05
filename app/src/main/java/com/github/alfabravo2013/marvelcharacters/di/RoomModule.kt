package com.github.alfabravo2013.marvelcharacters.di

import com.github.alfabravo2013.marvelcharacters.localstorage.MarvelDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {
    single { MarvelDatabase.getInstance(androidContext()).charactersDao }
}
