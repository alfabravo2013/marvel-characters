package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterListItem
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem

class CharactersRepository(private val remoteDataSource: CharactersRemoteDataSource) {

    suspend fun getCharactersPage(pageSize: Int): List<CharactersItem> {
        return remoteDataSource.getCharactersPage(pageSize).map { marvelCharacter ->
            marvelCharacter.toCharacterListItem()
        }
    }
}
