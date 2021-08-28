package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage

class CharactersRepository(private val remoteDataSource: CharactersRemoteDataSource) {

    suspend fun getNextPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource.getNextPage(pageSize).toCharacterItemPage()
    }

    suspend fun getPrevPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource.getPrevPage(pageSize).toCharacterItemPage()
    }

    suspend fun getCurrentPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource.getCurrentPage(pageSize).toCharacterItemPage()
    }
}
