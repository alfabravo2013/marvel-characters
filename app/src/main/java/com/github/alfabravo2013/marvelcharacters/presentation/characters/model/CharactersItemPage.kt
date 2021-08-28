package com.github.alfabravo2013.marvelcharacters.presentation.characters.model

data class CharactersItemPage(
    val prevOffset: Int?,
    val nextOffset: Int?,
    val characters: List<CharactersItem>
)
