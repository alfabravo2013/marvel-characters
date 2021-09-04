package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter

class DetailLocalDataSource {
    private val bookmarked = mutableMapOf<Int, MarvelCharacter>()

    fun addBookmark(marvelCharacter: MarvelCharacter) {
        bookmarked[marvelCharacter.id] = marvelCharacter
    }

    fun removeBookmark(marvelCharacter: MarvelCharacter) {
        bookmarked.remove(marvelCharacter.id)
    }

    fun isBookmarked(id: Int): Boolean = bookmarked.containsKey(id)
}
