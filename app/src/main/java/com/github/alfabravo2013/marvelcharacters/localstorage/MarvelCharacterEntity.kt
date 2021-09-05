package com.github.alfabravo2013.marvelcharacters.localstorage

data class MarvelCharacterEntity(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val bookmarked: Boolean,
)
