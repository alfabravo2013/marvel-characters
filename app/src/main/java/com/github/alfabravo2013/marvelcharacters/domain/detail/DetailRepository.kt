package com.github.alfabravo2013.marvelcharacters.domain.detail

import com.github.alfabravo2013.marvelcharacters.mappers.toDetail
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail

class DetailRepository(private val remoteDataSource: DetailRemoteDataSource) {

    suspend fun getCharacterById(characterId: Int): Detail {
        return remoteDataSource.getCharacterById(characterId).toDetail()
    }
}
