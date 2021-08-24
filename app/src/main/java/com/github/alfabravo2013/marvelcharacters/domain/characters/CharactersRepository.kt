package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharacterItemPage

class CharactersRepository(private val remoteDataSource: CharactersRemoteDataSource) {

    suspend fun getCharactersPage(pageSize: Int): CharacterItemPage {
        val page = remoteDataSource.getCharactersPage(pageSize)
        return page.toCharacterItemPage()
    }
}
