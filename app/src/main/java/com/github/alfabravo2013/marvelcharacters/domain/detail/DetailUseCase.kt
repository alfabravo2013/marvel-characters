package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail

class DetailUseCase(private val detailRepository: DetailRepository) {

    suspend fun getCharacterById(characterId: Int): Detail =
        detailRepository.getCharacterById(characterId)

    suspend fun switchBookmarkedStatus(): Boolean {
        if (isBookmarked()) {
            removeBookmark()
        } else {
            addBookmark()
        }

        return isBookmarked()
    }

    private suspend fun addBookmark() = detailRepository.addBookmark()

    private suspend fun removeBookmark() = detailRepository.removeBookmark()

    suspend fun isBookmarked(): Boolean = detailRepository.isBookmarked()
}
