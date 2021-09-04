package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.domain.characters.models.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.utils.MAX_PAGE_SIZE

class CharactersRemoteDataSource(private val marvelApi: MarvelApi) {
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
        val adjustedSize = if (currentOffset - pageSize < 0) currentOffset else pageSize
        return getCharactersPage(currentOffset - pageSize, adjustedSize)
    }

    suspend fun getFirstPage(pageSize: Int): MarvelCharacterPage {
        return getCharactersPage(0, pageSize)
    }

    private suspend fun getCharactersPage(offset: Int, pageSize: Int): MarvelCharacterPage {
        val response = if (queryText.isNotBlank()) {
            marvelApi.getCharactersPage(
                offset = offset.coerceAtLeast(0),
                limit = pageSize.coerceIn(0..MAX_PAGE_SIZE),
                name = queryText
            )
        } else {
            marvelApi.getCharactersPage(
                offset = offset.coerceAtLeast(0),
                limit = pageSize.coerceIn(0..MAX_PAGE_SIZE)
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
                    nextOffset = if (response.data.count < pageSize || currentOffset >= total) {
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
