package com.github.alfabravo2013.marvelcharacters.domain.characters.models

import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter

data class MarvelCharacterPage(
    val prevOffset: Int?,
    val nextOffset: Int?,
    val characters: List<MarvelCharacter>
)
