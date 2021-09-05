package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.domain.characters.models.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.localstorage.CharactersDao
import com.github.alfabravo2013.marvelcharacters.localstorage.entities.MarvelCharacterEntity
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import kotlin.math.max
import kotlin.math.min

class CharactersLocalDataSource(private val charactersDao: CharactersDao) {
    private var minPrevOffset: Int? = 0
    private var maxNextOffset: Int? = 0

    private val loadedCharacters = mutableSetOf<MarvelCharacter>()

    fun addPage(page: MarvelCharacterPage) {
        if (minPrevOffset != null) {
            minPrevOffset = if (page.prevOffset == null) {
                null
            } else {
                min(minPrevOffset!!, page.prevOffset)
            }
        }

        if (maxNextOffset != null) {
            maxNextOffset = if (page.nextOffset == null) {
                null
            } else {
                max(maxNextOffset!!, page.nextOffset)
            }
        }

        loadedCharacters.addAll(page.characters)
    }

    fun getCurrentPages(): MarvelCharacterPage {
        return MarvelCharacterPage(
            prevOffset = minPrevOffset,
            nextOffset = maxNextOffset,
            characters = loadedCharacters.sortedBy { it.name }.toList()
        )
    }

    fun clearCurrentPages() {
        loadedCharacters.clear()
        minPrevOffset = 0
        maxNextOffset = 0
    }

    suspend fun getBookmarked(): List<MarvelCharacterEntity> = charactersDao.getBookmarked()
}
