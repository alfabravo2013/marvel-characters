package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.domain.characters.models.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi

class CharactersRemoteDataSource(private val marvelApi: MarvelApi) {
    private val maxPageSize = 100

    private var queryText = ""

    private var currentOffset = 0
    private var total = Int.MAX_VALUE

    fun updateQueryText(text: String) {
        queryText = text
    }

    suspend fun getNextPage(pageSize: Int): MarvelCharacterPage {
        return getCharactersPage(currentOffset + pageSize, pageSize)
    }

    suspend fun getPrevPage(pageSize: Int): MarvelCharacterPage {
        return getCharactersPage(currentOffset - pageSize, pageSize)
    }

    suspend fun getFirstPage(pageSize: Int): MarvelCharacterPage {
        return getCharactersPage(0, pageSize)
    }

    private suspend fun getCharactersPage(offset: Int, pageSize: Int): MarvelCharacterPage {
        val response = if (queryText.isNotBlank()) {
            marvelApi.getCharactersPage(
                offset = offset,
                limit = pageSize.coerceIn(1..maxPageSize),
                name = queryText
            )
        } else {
            marvelApi.getCharactersPage(
                offset = offset,
                limit = pageSize.coerceIn(1..maxPageSize)
            )
        }

        when (response.code) {
            200 -> {
                total = response.data.total
                currentOffset = response.data.offset

                return MarvelCharacterPage(
                    prevOffset = if (currentOffset == 0) {
                        null
                    } else (currentOffset - pageSize).coerceAtLeast(0),
                    nextOffset = if (response.data.results.size < pageSize) {
                        null
                    } else {
                        currentOffset + pageSize
                    },
                    characters = response.data.results
                )
            }
            409 -> throw MarvelApi.BadRequestException()
            else -> throw MarvelApi.ApiException()
        }
    }
}
