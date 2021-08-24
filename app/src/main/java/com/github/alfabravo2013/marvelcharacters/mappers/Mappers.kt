package com.github.alfabravo2013.marvelcharacters.mappers

import com.github.alfabravo2013.marvelcharacters.domain.model.MarvelCharacterPage
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharacterItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem
import java.math.BigInteger
import java.security.MessageDigest

fun MarvelCharacter.toCharacterListItem(): CharactersItem {
    return CharactersItem(
        id = id,
        name = name,
        imageUrl = thumbnail.toString()
    )
}

fun MarvelCharacterPage.toCharacterItemPage(): CharacterItemPage {
    return CharacterItemPage(
        error = error,
        characters = characters.map { marvelCharacter -> marvelCharacter.toCharacterListItem() }
    )
}

fun String.toMD5Hash(): String {
    val md5 = MessageDigest.getInstance("MD5")
    return BigInteger(1, md5.digest(this.toByteArray()))
        .toString(16)
        .padStart(32, '0')
}
