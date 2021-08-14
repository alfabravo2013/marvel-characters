package com.github.alfabravo2013.marvelcharacters.domain

import android.content.Context
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

class LocalDataSource(private val context: Context) {
    private val characters = mutableListOf<MarvelCharacter>()
    private val limit = 20
    private var offset = 0
    private var total = Int.MAX_VALUE

    init {
        val jsonString = readJsonFromAsset()
        val list = getMavenCharacters(jsonString)
        characters.addAll(list)
        total = characters.size
    }

    fun getMoreCharacters(): List<MarvelCharacter> {
        val list = characters.asSequence()
            .drop(offset)
            .withIndex()
            .takeWhile { it.index <= limit && it.index <= total }
            .map { it.value }
            .toList()

        offset += list.size
        return list
    }

    fun hasMore(): Boolean = offset < total

    private fun readJsonFromAsset(): String {
        return context.assets.open(ASSET_NAME).bufferedReader().use { bufReader ->
            bufReader.readText()
        }
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

    companion object {
        private const val ASSET_NAME = "marvel-api-response.json"
        private const val EMPTY_ARRAY = "[]"
        private const val DATA_KEY = "data"
        private const val CHARACTER_LIST_KEY = "results"

        private val JSON = Json { ignoreUnknownKeys = true }
    }
}
