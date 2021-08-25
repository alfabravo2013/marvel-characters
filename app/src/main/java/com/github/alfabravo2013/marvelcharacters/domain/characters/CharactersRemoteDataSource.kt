package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter

class CharactersRemoteDataSource(private val marvelApi: MarvelApi) {
    private var currentOffset = 0
    private var total = Int.MAX_VALUE

    suspend fun getCharactersPage(pageSize: Int): List<MarvelCharacter> {
            val response = marvelApi.getCharactersPage(
                offset = currentOffset,
                limit = pageSize
            )

            if (response.code != 200) {
                throw IllegalArgumentException(response.code.toString())
            }

            total = response.data.total
            currentOffset += pageSize

            return response.data.results
    }
}
