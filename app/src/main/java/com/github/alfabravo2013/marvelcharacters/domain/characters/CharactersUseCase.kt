package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem

class CharactersUseCase(private val charactersRepository: CharactersRepository) {

    suspend fun getCharactersPage(pageSize: Int): List<CharactersItem> {
        return charactersRepository.getCharactersPage(pageSize)
    }
}
