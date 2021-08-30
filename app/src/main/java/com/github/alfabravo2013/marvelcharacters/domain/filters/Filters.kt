package com.github.alfabravo2013.marvelcharacters.domain.filters

import com.github.alfabravo2013.marvelcharacters.domain.characters.models.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import com.github.alfabravo2013.marvelcharacters.utils.IMAGE_UNAVAILABLE_EXTENSION
import com.github.alfabravo2013.marvelcharacters.utils.IMAGE_UNAVAILABLE_PATH

sealed class Filters {
    abstract val filter: (MarvelCharacter) -> Boolean

    object HasImage : Filters() {
        override val filter: (MarvelCharacter) -> Boolean = { it ->
            !it.thumbnail.path.contains(IMAGE_UNAVAILABLE_PATH) &&
                    !it.thumbnail.extension.contains(IMAGE_UNAVAILABLE_EXTENSION)
        }
    }

    object HasDescription : Filters() {
        override val filter: (MarvelCharacter) -> Boolean = { it ->
            it.description.isNotEmpty()
        }
    }
}

fun MarvelCharacterPage.applyFilters(filters: Collection<Filters>): MarvelCharacterPage {
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
