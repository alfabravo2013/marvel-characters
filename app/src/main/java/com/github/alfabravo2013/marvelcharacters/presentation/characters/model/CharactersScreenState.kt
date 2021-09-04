package com.github.alfabravo2013.marvelcharacters.presentation.characters.model

data class CharactersScreenState(
    val uniqueNamesFilterOn: Boolean = false,
    val hasDescriptionFilterOn: Boolean = false,
    val hasImageFilterOn: Boolean = false,
    val searchQuery: String = "",
)
