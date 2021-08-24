package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharacterItemPage

class CharactersUseCase(private val charactersRepository: CharactersRepository) {

    suspend fun getAllCharacters(pageSize: Int): CharacterItemPage {
        return charactersRepository.getCharactersPage(pageSize)
    }
}
