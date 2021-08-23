package com.github.alfabravo2013.marvelcharacters.domain.model

import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter

data class MarvelCharacterPage(
    val error: String = "",
    val characters: List<MarvelCharacter> = emptyList()
)
