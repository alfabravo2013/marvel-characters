package com.github.alfabravo2013.marvelcharacters

import android.app.Application
import com.github.alfabravo2013.marvelcharacters.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarvelApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MarvelApplication)
            modules(appModule)
        }
    }
}
