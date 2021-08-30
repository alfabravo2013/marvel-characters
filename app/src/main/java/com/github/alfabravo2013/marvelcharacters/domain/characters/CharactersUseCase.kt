package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
import com.github.alfabravo2013.marvelcharacters.domain.filters.Filters
import com.github.alfabravo2013.marvelcharacters.utils.DEFAULT_PAGE_SIZE

class CharactersUseCase(private val charactersRepository: CharactersRepository) {
    private val filters: MutableSet<Filters> = mutableSetOf()

    fun updateQueryText(text: String?): String {
        if (text.isNullOrBlank()) {
            charactersRepository.updateQueryText("")
            return ""
        }

        charactersRepository.updateQueryText(text)
        return text
    }

    suspend fun getNextPage(): CharactersItemPage {
        charactersRepository.requestNextPage(DEFAULT_PAGE_SIZE)
        return charactersRepository.getCurrentPages(filters)
    }

    suspend fun getPrevPage(): CharactersItemPage {
        charactersRepository.requestPrevPage(DEFAULT_PAGE_SIZE)
        return charactersRepository.getCurrentPages(filters)
    }

    suspend fun getFirstPage(): CharactersItemPage {
        charactersRepository.requestFirstPage(DEFAULT_PAGE_SIZE)
        return charactersRepository.getCurrentPages(filters)
    }

    fun getCurrentPages(): CharactersItemPage {
        return charactersRepository.getCurrentPages(filters)
    }

    fun addWithImageFilter() {
        filters.add(Filters.HasImage)
    }

    fun removeWithImageFilter() {
        filters.remove(Filters.HasImage)
    }

    fun addWithDescriptionFilter() {
        filters.add(Filters.HasDescription)
    }

    fun removeWithDescriptionFilter() {
        filters.remove(Filters.HasDescription)
    }
}
