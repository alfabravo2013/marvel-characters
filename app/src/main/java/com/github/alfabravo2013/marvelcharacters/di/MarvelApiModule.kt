package com.github.alfabravo2013.marvelcharacters.di

import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.MarvelRequestInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

@ExperimentalSerializationApi
val marvelApiModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(MarvelRequestInterceptor())
            .build()
    }

    single<Retrofit> {
        val contentType = MediaType.get("application/json")
        val json = Json { ignoreUnknownKeys = true }

        Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    single<MarvelApi> { get<Retrofit>().create(MarvelApi::class.java) }
}
