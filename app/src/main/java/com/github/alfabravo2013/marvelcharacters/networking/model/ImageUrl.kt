package com.github.alfabravo2013.marvelcharacters.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageUrl(
    val path: String,
    val extension: String
) {
    override fun toString(): String = "$path.$extension"
}
