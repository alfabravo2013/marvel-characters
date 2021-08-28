package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage

class CharactersUseCase(private val charactersRepository: CharactersRepository) {
    private val nameMatcher = "[A-Za-z\\s]+".toRegex()

    fun updateQueryText(text: String?): Boolean {
        if (text.isNullOrBlank() || !text.matches(nameMatcher)) {
            charactersRepository.updateQueryText("")
            return false
        }

        charactersRepository.updateQueryText(text)
        return true
    }

    suspend fun getNextPage(pageSize: Int): CharactersItemPage {
        return charactersRepository.getNextPage(pageSize)
    }

    suspend fun getPrevPage(pageSize: Int): CharactersItemPage {
        return charactersRepository.getPrevPage(pageSize)
    }

    suspend fun getFirstPage(pageSize: Int): CharactersItemPage {
        return charactersRepository.getFirstPage(pageSize)
    }
}
