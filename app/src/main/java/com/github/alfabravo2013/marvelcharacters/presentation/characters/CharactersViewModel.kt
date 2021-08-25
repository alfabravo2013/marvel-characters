package com.github.alfabravo2013.marvelcharacters.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersUseCase
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItem
import com.github.alfabravo2013.marvelcharacters.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(private val charactersUseCase: CharactersUseCase) : ViewModel() {
    private var isLoading = false

    private val _onEvent = SingleLiveEvent<OnEvent>()
    val onEvent: SingleLiveEvent<OnEvent> get() = _onEvent

    fun getCharactersPage() = viewModelScope.launch {
        if (isLoading) {
            return@launch
        }

        isLoading = true
        _onEvent.value = OnEvent.ShowLoading

        runCatching {
            val characters = withContext(Dispatchers.IO) {
                charactersUseCase.getAllCharacters(20)
            }
            _onEvent.value = OnEvent.SubmitData(characters)
        }.onFailure { error ->
            showError(error)
        }

        _onEvent.value = OnEvent.HideLoading
        isLoading = false
    }

    private fun showError(ex: Throwable) {
        when (ex) {
            is IllegalArgumentException -> _onEvent.value =
                OnEvent.ShowError(R.string.page_not_found)
            else -> _onEvent.value = OnEvent.ShowError(R.string.unknown_error)
        }
    }

    sealed class OnEvent {
        object ShowLoading : OnEvent()
        object HideLoading : OnEvent()
        data class ShowError(val errorId: Int) : OnEvent()
        data class SubmitData(val data: List<CharactersItem>) : OnEvent()
    }
}
