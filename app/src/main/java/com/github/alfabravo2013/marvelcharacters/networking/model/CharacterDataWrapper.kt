package com.github.alfabravo2013.marvelcharacters.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDataWrapper(
    val code: Int,
    val status: String,
    val data: CharacterDataContainer
)
