package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.mappers.toDetail
import com.github.alfabravo2013.marvelcharacters.mappers.toEntity
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail

class DetailRepository(
    private val detailRemoteDataSource: DetailRemoteDataSource,
    private val detailLocalDataSource: DetailLocalDataSource
) {

    suspend fun getCharacterById(characterId: Int): Detail {
        val isBookmarked = detailLocalDataSource.isBookmarked(characterId)
        val entity = detailRemoteDataSource.getCharacterById(characterId).toEntity(isBookmarked)
        detailLocalDataSource.setCurrentMarvelCharacter(entity)
        return detailLocalDataSource.getCurrentMarvelCharacter().toDetail()
    }

    suspend fun addBookmark() {
        detailLocalDataSource.addBookmark()
    }

    suspend fun removeBookmark() {
        detailLocalDataSource.removeBookmark()
    }

    suspend fun isBookmarked(): Boolean {
        val currentCharacterId = detailLocalDataSource.getCurrentMarvelCharacter().id
        return detailLocalDataSource.isBookmarked(currentCharacterId)
    }
}
