package com.github.alfabravo2013.marvelcharacters.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersUseCase

class CharactersViewModel(
    private val charactersUseCase: CharactersUseCase
) : ViewModel() {

    private val pageSize = 20

    val characters = charactersUseCase.getCharactersPage(pageSize).cachedIn(viewModelScope)
}
