package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter

class DetailRemoteDataSource(private val marvelApi: MarvelApi) {
    private var currentMarvelCharacter: MarvelCharacter? = null

    suspend fun getCharacterById(characterId: Int): MarvelCharacter {
            val response = marvelApi.getCharacterById(characterId)

            when (response.code) {
                200 -> return response.data.results.first().also { currentMarvelCharacter = it }
                404 -> throw MarvelApi.NotFoundException().also { currentMarvelCharacter = null }
                else -> throw MarvelApi.ApiException().also { currentMarvelCharacter = null }
            }
    }

    fun getCurrentMarvelCharacter(): MarvelCharacter {
        return currentMarvelCharacter ?: error("No current MarvelCharacter available")
    }
}
