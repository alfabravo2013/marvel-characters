package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.domain.characters.models.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.mappers.toCharacterItemPage
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
import com.github.alfabravo2013.marvelcharacters.utils.IMAGE_UNAVAILABLE_EXTENSION
import com.github.alfabravo2013.marvelcharacters.utils.IMAGE_UNAVAILABLE_PATH

class CharactersRepository(private val remoteDataSource: CharactersRemoteDataSource) {

    private val filters: MutableSet<Filter> = mutableSetOf()

    fun addFilter(filter: Filter) {
        filters.add(filter)
    }

    fun removeFilter(filter: Filter) {
        filters.remove(filter)
    }

    fun updateQueryText(text: String) = remoteDataSource.updateQueryText(text)

    suspend fun getNextPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource
            .getNextPage(pageSize)
            .applyFilters()
            .toCharacterItemPage()
    }

    suspend fun getPrevPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource
            .getPrevPage(pageSize)
            .applyFilters()
            .toCharacterItemPage()
    }

    suspend fun getFirstPage(pageSize: Int): CharactersItemPage {
        return remoteDataSource
            .getFirstPage(pageSize)
            .applyFilters()
            .toCharacterItemPage()
    }

    private fun MarvelCharacterPage.applyFilters(): MarvelCharacterPage {
        return if (filters.isEmpty()) {
            this
        } else {
            this.copy(
                characters = this.characters.filter { character ->
                    filters.all { it.filter(character) }
                }
            )
        }
    }

    sealed class Filter {
        abstract val filter: (MarvelCharacter) -> Boolean

        object HasImage : Filter() {
            override val filter: (MarvelCharacter) -> Boolean = { it ->
                !it.thumbnail.path.contains(IMAGE_UNAVAILABLE_PATH) &&
                        !it.thumbnail.extension.contains(IMAGE_UNAVAILABLE_EXTENSION)
            }
        }

        object HasDescription : Filter() {
            override val filter: (MarvelCharacter) -> Boolean = { it ->
                it.description.isNotEmpty()
            }
        }
    }
}
