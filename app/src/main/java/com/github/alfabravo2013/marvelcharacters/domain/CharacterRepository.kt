package com.github.alfabravo2013.marvelcharacters.domain

import com.github.alfabravo2013.marvelcharacters.networking.model.toCharacterListItem
import com.github.alfabravo2013.marvelcharacters.presentation.characterlist.model.CharacterListItem

class CharacterRepository(private val localDataSource: LocalDataSource) {

    fun getMoreCharacters(): List<CharacterListItem> =
        localDataSource.getMoreCharacters().map { it.toCharacterListItem() }

    fun hasMore(): Boolean = localDataSource.hasMore()
}
