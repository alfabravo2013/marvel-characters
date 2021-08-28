package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage

class CharactersUseCase(private val charactersRepository: CharactersRepository) {

    suspend fun getNextPage(pageSize: Int): CharactersItemPage {
        return charactersRepository.getNextPage(pageSize)
    }

    suspend fun getPrevPage(pageSize: Int): CharactersItemPage {
        return charactersRepository.getPrevPage(pageSize)
    }

    suspend fun getCurrentPage(pageSize: Int): CharactersItemPage {
        return charactersRepository.getCurrentPage(pageSize)
    }
}
