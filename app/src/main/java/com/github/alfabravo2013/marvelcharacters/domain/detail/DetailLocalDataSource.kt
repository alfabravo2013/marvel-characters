package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.localstorage.LocalStorage
import com.github.alfabravo2013.marvelcharacters.localstorage.MarvelCharacterEntity

class DetailLocalDataSource(private val localStorage: LocalStorage) {
    private var currentMarvelCharacter: MarvelCharacterEntity? = null

    fun getCurrentMarvelCharacter(): MarvelCharacterEntity {
        return currentMarvelCharacter ?: error("No current MarvelCharacter available")
    }

    fun setCurrentMarvelCharacter(character: MarvelCharacterEntity) {
        currentMarvelCharacter = character
    }

    fun addBookmark() {
        localStorage.addBookmark(getCurrentMarvelCharacter())
    }

    fun removeBookmark() {
        localStorage.removeBookmarkById(getCurrentMarvelCharacter().id)
    }

    fun isBookmarked(id: Int): Boolean = localStorage.isBookmarked(id)
}
