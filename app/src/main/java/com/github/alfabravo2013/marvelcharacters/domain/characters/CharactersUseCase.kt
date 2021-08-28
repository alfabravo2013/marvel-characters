package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersRepository.Filter
import com.github.alfabravo2013.marvelcharacters.utils.DEFAULT_PAGE_SIZE

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

    suspend fun getNextPage(): CharactersItemPage {
        return charactersRepository.getNextPage(DEFAULT_PAGE_SIZE)
    }

    suspend fun getPrevPage(): CharactersItemPage {
        return charactersRepository.getPrevPage(DEFAULT_PAGE_SIZE)
    }

    suspend fun getFirstPage(): CharactersItemPage {
        return charactersRepository.getFirstPage(DEFAULT_PAGE_SIZE * 2)
    }

    fun addWithImageFilter() {
        charactersRepository.addFilter(Filter.HasImage)
    }

    fun removeWithImageFilter() {
        charactersRepository.removeFilter(Filter.HasImage)
    }

    fun addWithDescriptionFilter() {
        charactersRepository.addFilter(Filter.HasDescription)
    }

    fun removeWithDescriptionFilter() {
        charactersRepository.removeFilter(Filter.HasDescription)
    }
}
