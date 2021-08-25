package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.domain.model.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi

class DetailRemoteDataSource(private val marvelApi: MarvelApi) {

    suspend fun getCharacterById(characterId: Int): MarvelCharacterPage {
        try {
            val response = marvelApi.getCharacterById(characterId)
            if (response.code != 200) {
                return MarvelCharacterPage(error = response.status)
            }

            return MarvelCharacterPage(characters = response.data.results)

        } catch (ex: Exception) {
            return MarvelCharacterPage(error = ex.message ?: "unknown error")
        }
    }
}
