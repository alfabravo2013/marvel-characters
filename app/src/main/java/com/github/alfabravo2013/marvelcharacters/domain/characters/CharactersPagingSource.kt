package com.github.alfabravo2013.marvelcharacters.domain.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter

class CharactersPagingSource(
    private val remoteDataSource: CharactersRemoteDataSource
) : PagingSource<Int, MarvelCharacter>() {
    override fun getRefreshKey(state: PagingState<Int, MarvelCharacter>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarvelCharacter> {

        return try {
            val offset = params.key ?: 0
            val response = remoteDataSource.getCharacters(offset, params.loadSize)

            val prevOffset = if (offset > 0) {
                (offset - params.loadSize).coerceAtLeast(0)
            } else {
                null
            }

            val nextOffset = if (response.isNotEmpty()) {
                offset + params.loadSize
            } else {
                null
            }

            LoadResult.Page(data = response, prevKey = prevOffset, nextKey = nextOffset)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}
