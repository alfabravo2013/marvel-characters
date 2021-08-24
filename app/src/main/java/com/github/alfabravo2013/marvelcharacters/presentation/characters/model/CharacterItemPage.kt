package com.github.alfabravo2013.marvelcharacters.presentation.characters.model

data class CharacterItemPage(
    val error: String = "",
    val characters: List<CharactersItem> = emptyList()
)
