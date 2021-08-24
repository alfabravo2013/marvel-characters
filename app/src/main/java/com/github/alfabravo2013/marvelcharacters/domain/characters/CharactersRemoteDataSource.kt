package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.domain.model.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi

class CharactersRemoteDataSource(private val marvelApi: MarvelApi) {
    private var currentOffset = 0
    private var total = Int.MAX_VALUE

    suspend fun getCharactersPage(pageSize: Int): MarvelCharacterPage {
        try {
            val response = marvelApi.getCharactersPage(
                offset = currentOffset,
                limit = pageSize
            )

            if (response.code != 200) {
                return MarvelCharacterPage(error = response.status)
            }

            total = response.data.total
            currentOffset += pageSize

            return MarvelCharacterPage(characters = response.data.results)

        } catch (ex: Exception) {
            return MarvelCharacterPage(error = ex.message ?: "unknown error")
        }
    }
}
