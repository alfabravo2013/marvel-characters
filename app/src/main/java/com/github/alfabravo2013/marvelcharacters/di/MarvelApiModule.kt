package com.github.alfabravo2013.marvelcharacters.di

import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.MarvelRequestInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

val marvelApiModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(MarvelRequestInterceptor())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .client(get())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    single<MarvelApi> { get<Retrofit>().create(MarvelApi::class.java) }
}
