package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import java.lang.IllegalArgumentException

class DetailRemoteDataSource(private val marvelApi: MarvelApi) {

    suspend fun getCharacterById(characterId: Int): MarvelCharacter {
            val response = marvelApi.getCharacterById(characterId)
            if (response.code != 200) {
                throw IllegalArgumentException(response.code.toString())
            }

            return response.data.results.first()
    }
}
