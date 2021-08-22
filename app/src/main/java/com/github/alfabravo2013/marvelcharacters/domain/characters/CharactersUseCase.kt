package com.github.alfabravo2013.marvelcharacters.domain.characters

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem

class CharactersUseCase(private val charactersRepository: CharactersRepository) {

    fun getCharactersPage(pageSize: Int): LiveData<PagingData<CharactersItem>> {
        return charactersRepository.getCharactersPage(pageSize)
    }
}
