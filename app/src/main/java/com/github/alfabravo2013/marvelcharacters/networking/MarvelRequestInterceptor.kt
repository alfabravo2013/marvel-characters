package com.github.alfabravo2013.marvelcharacters.networking

import com.github.alfabravo2013.marvelcharacters.BuildConfig
import com.github.alfabravo2013.marvelcharacters.mappers.toMD5Hash
import okhttp3.Interceptor
import okhttp3.Response

class MarvelRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val ts = System.currentTimeMillis().toString()
        val apikey: String = BuildConfig.PUBLIC_API_KEY
        val accessString = "$ts${BuildConfig.PRIVATE_API_KEY}${BuildConfig.PUBLIC_API_KEY}"

        val httpUrl = originalRequest.url().newBuilder()
            .addQueryParameter("ts", ts)
            .addQueryParameter("apikey", apikey)
            .addQueryParameter("hash", accessString.toMD5Hash())
            .build()

        val builder = originalRequest.newBuilder().url(httpUrl)
        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }
}
