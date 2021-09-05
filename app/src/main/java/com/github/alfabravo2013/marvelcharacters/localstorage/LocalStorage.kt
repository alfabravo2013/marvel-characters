package com.github.alfabravo2013.marvelcharacters.localstorage

class LocalStorage {
    private val bookmarked = mutableMapOf<Int, MarvelCharacterEntity>()

    fun addBookmark(marvelCharacter: MarvelCharacterEntity) {
        bookmarked[marvelCharacter.id] = marvelCharacter
    }

    fun removeBookmarkById(id: Int) {
        bookmarked.remove(id)
    }

    fun isBookmarked(id: Int): Boolean = bookmarked.containsKey(id)

    fun getBookmarked(): List<MarvelCharacterEntity> =
        bookmarked.values.sortedBy { it.name }.toList()
}
