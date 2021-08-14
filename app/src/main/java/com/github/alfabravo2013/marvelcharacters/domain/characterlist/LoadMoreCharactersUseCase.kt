package com.github.alfabravo2013.marvelcharacters.domain.characterlist

import com.github.alfabravo2013.marvelcharacters.domain.CharacterRepository
import com.github.alfabravo2013.marvelcharacters.presentation.characterlist.model.CharacterListItem

class LoadMoreCharactersUseCase(private val characterRepository: CharacterRepository) {

    fun getMoreCharacters(): List<CharacterListItem> {
        return if (characterRepository.hasMore()) {
            characterRepository.getMoreCharacters()
        } else {
            emptyList()
        }
    }
}
