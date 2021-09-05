package com.github.alfabravo2013.marvelcharacters.mappers

import com.github.alfabravo2013.marvelcharacters.domain.characters.models.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.localstorage.entities.MarvelCharacterEntity
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail
import java.math.BigInteger
import java.security.MessageDigest

fun MarvelCharacter.toCharacterListItem(): CharactersItem {
    return CharactersItem(
        id = id,
        name = name,
        imageUrl = thumbnail.toString(),
        bookmarked = false, // FIXME: 05.09.2021 remove once CharactersLocalDataSource refactored to use entity
    )
}

fun MarvelCharacter.toDetail(bookmarked: Boolean = false): Detail {
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

fun MarvelCharacter.toEntity(bookmarked: Boolean = false): MarvelCharacterEntity {
    return MarvelCharacterEntity(
        id = id,
        name = name,
        description = if (description.isEmpty()) {
            "Description not available"
        } else {
            description
        },
        imageUrl = thumbnail.toString(),
        bookmarked = bookmarked,
    )
}

fun MarvelCharacterEntity.toDetail(): Detail {
    return Detail(
        name = name,
        description = description,
        imageUrl = imageUrl,
        bookmarked = bookmarked,
    )
}

fun MarvelCharacterEntity.toCharacterItem(): CharactersItem {
    return CharactersItem(
        id = id,
        name = name,
        imageUrl = imageUrl,
        bookmarked = bookmarked,
    )
}

fun MarvelCharacterPage.toCharacterItemPage(): CharactersItemPage {
    return CharactersItemPage(
        prevOffset = prevOffset,
        nextOffset = nextOffset,
        characters = characters.map { it.toCharacterListItem() }
    )
}

fun String.toMD5Hash(): String {
    val md5 = MessageDigest.getInstance("MD5")
    return BigInteger(1, md5.digest(this.toByteArray()))
        .toString(16)
        .padStart(32, '0')
}
