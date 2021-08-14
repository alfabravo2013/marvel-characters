package com.github.alfabravo2013.marvelcharacters.networking.model

import com.github.alfabravo2013.marvelcharacters.presentation.characterlist.model.CharacterListItem

fun MarvelCharacter.toCharacterListItem(): CharacterListItem {
    return CharacterListItem(
        id = id,
        name = name,
        imageUrl = "${thumbnail.path}.${thumbnail.extension}"
    )
}

fun ImageUrl.toImageUrl(): String = "$path.$extension"
