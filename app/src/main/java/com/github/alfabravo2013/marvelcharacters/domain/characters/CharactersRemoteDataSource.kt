package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

class CharactersRemoteDataSource(private val marvelApi: MarvelApi) {
    private var limit = 0
    private var offset = 0
    private var total = Int.MAX_VALUE

    suspend fun getCharacters(offset: Int, limit: Int): List<MarvelCharacter> {
        updateOffset(offset)
        updateLimit(limit)

        val response = marvelApi.getCharacters(limit = this.limit, offset = this.offset)
        val results = if (response.isSuccessful) {
            getMavenCharacters(response.body() ?: EMPTY_OBJECT)
        } else {
            emptyList()
        }

        updateTotal(response.body() ?: EMPTY_OBJECT)
        return results
    }

    private fun getMavenCharacters(body: String): List<MarvelCharacter> {
        val element = JSON
            .parseToJsonElement(body)
            .jsonObject[DATA_KEY]
            ?.jsonObject
            ?.get(CHARACTER_LIST_KEY)
            ?: JSON.parseToJsonElement(EMPTY_ARRAY)

        return JSON.decodeFromJsonElement(element)
    }

    private fun getTotal(body: String): Int {
        val element = JSON.parseToJsonElement(body)
            .jsonObject[DATA_KEY]
            ?.jsonObject
            ?.get(TOTAL_KEY)

        return element?.let { JSON.decodeFromJsonElement(it) } ?: 0
    }

    private fun updateOffset(offset: Int) {
        this.offset = offset.coerceAtMost(total)
    }

    private fun updateLimit(limit: Int) {
        this.limit = limit.coerceAtMost(total - this.offset)
    }

    private fun updateTotal(body: String) {
        this.total = getTotal(body)
    }

    companion object {
        private const val EMPTY_ARRAY = "[]"
        private const val EMPTY_OBJECT = "{}"
        private const val DATA_KEY = "data"
        private const val TOTAL_KEY = "total"
        private const val CHARACTER_LIST_KEY = "results"

        private val JSON = Json { ignoreUnknownKeys = true }
    }
}
