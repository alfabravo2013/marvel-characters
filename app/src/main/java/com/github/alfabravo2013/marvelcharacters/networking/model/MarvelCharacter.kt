package com.github.alfabravo2013.marvelcharacters.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ImageUrl
)
