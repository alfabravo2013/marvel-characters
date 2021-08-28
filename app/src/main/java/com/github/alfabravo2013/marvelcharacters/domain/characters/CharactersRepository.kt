package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage

class CharactersRepository(private val remoteDataSource: CharactersRemoteDataSource) {

    fun updateQueryText(text: String) = remoteDataSource.updateQueryText(text)

    suspend fun getNextPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource.getNextPage(pageSize).toCharacterItemPage()
    }

    suspend fun getPrevPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource.getPrevPage(pageSize).toCharacterItemPage()
    }

    suspend fun getFirstPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource.getFirstPage(pageSize).toCharacterItemPage()
    }
}
