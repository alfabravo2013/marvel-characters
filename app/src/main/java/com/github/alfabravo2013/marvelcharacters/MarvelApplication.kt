package com.github.alfabravo2013.marvelcharacters

import android.app.Application
import com.github.alfabravo2013.marvelcharacters.di.charactersModule
import com.github.alfabravo2013.marvelcharacters.di.marvelApiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarvelApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MarvelApplication)
            modules(listOf(charactersModule, marvelApiModule))
        }
    }
}
