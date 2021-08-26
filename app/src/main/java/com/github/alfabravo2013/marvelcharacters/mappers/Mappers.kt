package com.github.alfabravo2013.marvelcharacters.mappers

import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail
import java.math.BigInteger
import java.security.MessageDigest

fun MarvelCharacter.toCharacterListItem(): CharactersItem {
    return CharactersItem(
        id = id,
        name = name,
        imageUrl = thumbnail.toString()
    )
}

fun MarvelCharacter.toDetail(): Detail {
    return Detail(
        name = name,
        description = if (description.isEmpty()) {
            "Description not available"
        } else {
            description
        },
        imageUrl = thumbnail.toString()
    )
}

fun String.toMD5Hash(): String {
    val md5 = MessageDigest.getInstance("MD5")
    return BigInteger(1, md5.digest(this.toByteArray()))
        .toString(16)
        .padStart(32, '0')
}
