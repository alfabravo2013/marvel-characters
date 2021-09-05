package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter

class DetailRemoteDataSource(private val marvelApi: MarvelApi) {

    suspend fun getCharacterById(characterId: Int): MarvelCharacter {
            val response = marvelApi.getCharacterById(characterId)

            when (response.code) {
                200 -> return response.data.results.first()
                404 -> throw MarvelApi.NotFoundException()
                else -> throw MarvelApi.ApiException()
            }
    }
}
