package com.github.alfabravo2013.marvelcharacters.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersUseCase
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(private val charactersUseCase: CharactersUseCase) : ViewModel() {
    private var isLoading = false

    private val _onEvent = MutableLiveData<OnEvent>()
    val onEvent: LiveData<OnEvent> get() = _onEvent

    init {
        onGetCharactersPage()
    }

    fun onGetCharactersPage() = viewModelScope.launch {
        if (isLoading) {
            return@launch
        }

        _onEvent.value = OnEvent.ShowLoading
        isLoading = true
        val characters = withContext(Dispatchers.IO) {
            charactersUseCase.getAllCharacters(20)
        }
        _onEvent.value = OnEvent.HideLoading
        isLoading = false

        if (characters.error.isNotEmpty()) {
            _onEvent.value = OnEvent.ShowError(characters.error)
        } else {
            _onEvent.value = OnEvent.SubmitData(characters.characters)
        }
    }

    sealed class OnEvent {
        object ShowLoading : OnEvent()
        object HideLoading : OnEvent()
        data class ShowError(val error: String) : OnEvent()
        data class SubmitData(val data: List<CharactersItem>) : OnEvent()
    }
}
