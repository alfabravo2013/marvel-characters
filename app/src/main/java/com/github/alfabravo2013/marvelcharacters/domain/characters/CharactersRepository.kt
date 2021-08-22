package com.github.alfabravo2013.marvelcharacters.domain.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterListItem
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem

class CharactersRepository(private val remoteDataSource: CharactersRemoteDataSource) {

    fun getCharactersPage(pageSize: Int): LiveData<PagingData<CharactersItem>> {
        return Pager(config = PagingConfig(
            pageSize = pageSize,
            maxSize = pageSize * 4,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { CharactersPagingSource(remoteDataSource) }
        ).liveData.map { pagingData ->
            pagingData.map { character ->
                character.toCharacterListItem()
            }
        }
    }
}
