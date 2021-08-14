package com.github.alfabravo2013.marvelcharacters.presentation.characterlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alfabravo2013.marvelcharacters.domain.characterlist.LoadMoreCharactersUseCase
import com.github.alfabravo2013.marvelcharacters.presentation.characterlist.model.CharacterListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterListViewModel(
    private val loadMoreCharactersUseCase: LoadMoreCharactersUseCase
) : ViewModel() {

    private val _onEvent = MutableLiveData<OnEvent>()
    val onEvent: LiveData<OnEvent> get() = _onEvent

    init {
        getMoreCharacters()
    }

    fun getMoreCharacters() = viewModelScope.launch {
        _onEvent.value = OnEvent.ShowLoading
        val moreCharacters = withContext(Dispatchers.IO) {
            loadMoreCharactersUseCase.getMoreCharacters()
        }
        _onEvent.value = OnEvent.HideLoading

        if (moreCharacters.isNotEmpty()) {
            _onEvent.value = OnEvent.CharactersLoaded(moreCharacters)
        }
    }

    sealed class OnEvent {
        object ShowLoading : OnEvent()
        object HideLoading : OnEvent()
        data class CharactersLoaded(val characters: List<CharacterListItem>) : OnEvent()
    }
}
