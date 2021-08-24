package com.github.alfabravo2013.marvelcharacters.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDataContainer(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val results: List<MarvelCharacter>
)
