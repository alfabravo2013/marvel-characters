package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.localstorage.CharactersDao
import com.github.alfabravo2013.marvelcharacters.localstorage.entities.MarvelCharacterEntity

class DetailLocalDataSource(private val charactersDao: CharactersDao) {
    private var currentMarvelCharacter: MarvelCharacterEntity? = null

    fun getCurrentMarvelCharacter(): MarvelCharacterEntity {
        return currentMarvelCharacter ?: error("No current MarvelCharacter available")
    }

    fun setCurrentMarvelCharacter(character: MarvelCharacterEntity) {
        currentMarvelCharacter = character
    }

    suspend fun addBookmark() {
        charactersDao.insert(getCurrentMarvelCharacter().copy(bookmarked = true))
    }

    suspend fun removeBookmark() {
        charactersDao.deleteById(getCurrentMarvelCharacter().id)
    }

    suspend fun isBookmarked(id: Int): Boolean = charactersDao.findBookmarkedById(id) != null
}
