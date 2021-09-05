package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.domain.filters.Filters
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
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
        // FIXME: 04.09.2021 when both filters are on, no further paging is at k or m letter,
        //  add onScroll listener to attempt scroll further
        var page: CharactersItemPage
        var count = 0

        while (true) {
            charactersRepository.requestNextPage(DEFAULT_PAGE_SIZE)
            page = charactersRepository.getCurrentPages(filters)
            if (page.characters.size - count < 8 && page.nextOffset != null) {
                count += page.characters.size
                continue
            }

            count += page.characters.size
            break
        }

        return page
    }

    suspend fun getBookmarked() = charactersRepository.getBookmarked()

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
