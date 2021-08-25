package com.github.alfabravo2013.marvelcharacters

import android.app.Application
import com.github.alfabravo2013.marvelcharacters.di.charactersModule
import com.github.alfabravo2013.marvelcharacters.di.detailModule
import com.github.alfabravo2013.marvelcharacters.di.marvelApiModule
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarvelApplication : Application() {

    @ExperimentalSerializationApi
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MarvelApplication)
            modules(listOf(charactersModule, marvelApiModule, detailModule))
        }
    }
}
