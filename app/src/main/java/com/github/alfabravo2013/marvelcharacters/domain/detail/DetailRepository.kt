package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.mappers.toDetail
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail

class DetailRepository(
    private val detailRemoteDataSource: DetailRemoteDataSource,
    private val detailLocalDataSource: DetailLocalDataSource
) {

    suspend fun getCharacterById(characterId: Int): Detail {
        val isBookmarked = detailLocalDataSource.isBookmarked(characterId)
        return detailRemoteDataSource.getCharacterById(characterId).toDetail(isBookmarked)
    }

    fun addBookmark() {
        detailLocalDataSource.addBookmark(detailRemoteDataSource.getCurrentMarvelCharacter())
    }

    fun removeBookmark() {
        detailLocalDataSource.removeBookmark(detailRemoteDataSource.getCurrentMarvelCharacter())
    }

    fun isBookmarked(): Boolean {
        val currentCharacterId = detailRemoteDataSource.getCurrentMarvelCharacter().id
        return detailLocalDataSource.isBookmarked(currentCharacterId)
    }
}
