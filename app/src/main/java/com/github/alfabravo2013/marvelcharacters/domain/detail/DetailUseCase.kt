package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail

class DetailUseCase(private val repository: DetailRepository) {

    suspend fun getCharacterById(characterId: Int): Detail =
        repository.getCharacterById(characterId)
}
