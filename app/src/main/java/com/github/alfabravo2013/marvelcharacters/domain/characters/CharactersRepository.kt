package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.domain.filters.Filters
import com.github.alfabravo2013.marvelcharacters.domain.filters.applyFilters
import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterItem
import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage

class CharactersRepository(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val localDataSource: CharactersLocalDataSource
) {
    fun updateQueryText(text: String) = remoteDataSource.updateQueryText(text)

    suspend fun requestNextPage(pageSize: Int) {
        val page = remoteDataSource.getNextPage(pageSize)
        localDataSource.addPage(page)
    }

    suspend fun requestPrevPage(pageSize: Int) {
        val page = remoteDataSource.getPrevPage(pageSize)
        localDataSource.addPage(page)
    }

    suspend fun requestFirstPage(pageSize: Int) {
        localDataSource.clearCurrentPages()
        val page = remoteDataSource.getFirstPage(pageSize)
        localDataSource.addPage(page)
    }

    fun getCurrentPages(filters: Collection<Filters>): CharactersItemPage =
        localDataSource
            .getCurrentPages()
            .applyFilters(filters)
            .toCharacterItemPage()

    suspend fun getBookmarked(): CharactersItemPage {
        return CharactersItemPage(
            prevOffset = null,
            nextOffset = null,
            characters = localDataSource.getBookmarked().map { it.toCharacterItem() }
        )
    }
}
