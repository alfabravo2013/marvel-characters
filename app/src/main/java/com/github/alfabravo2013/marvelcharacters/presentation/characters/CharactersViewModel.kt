package com.github.alfabravo2013.marvelcharacters.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersUseCase
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
import com.github.alfabravo2013.marvelcharacters.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(private val charactersUseCase: CharactersUseCase) : ViewModel() {
    private var isLoading = false

    private val _onEvent = SingleLiveEvent<OnEvent>()
    val onEvent: SingleLiveEvent<OnEvent> get() = _onEvent

    init {
        getCharactersPage(DIRECTION.CURRENT)
    }

    fun updateQueryText(text: String?): Boolean {
        return charactersUseCase.updateQueryText(text)
    }

    fun onToggleWithImageFilter(isChecked: Boolean) {
        if (isChecked) {
            charactersUseCase.addWithImageFilter()
        } else {
            charactersUseCase.removeWithImageFilter()
        }
    }

    fun onToggleWithDescriptionFilter(isChecked: Boolean) {
        if (isChecked) {
            charactersUseCase.addWithDescriptionFilter()
        } else {
            charactersUseCase.removeWithDescriptionFilter()
        }
    }

    fun getFirstPage() {
        _onEvent.value = OnEvent.CleanList
        getCharactersPage(DIRECTION.CURRENT)
    }

    fun getCharactersPage(direction: DIRECTION) = viewModelScope.launch {
        if (isLoading) {
            return@launch
        }

        isLoading = true
        _onEvent.value = OnEvent.ShowLoading

        runCatching {
            withContext(Dispatchers.IO) {
                when (direction) {
                    DIRECTION.NEXT -> charactersUseCase.getNextPage()
                    DIRECTION.CURRENT -> charactersUseCase.getFirstPage()
                    DIRECTION.PREVIOUS -> charactersUseCase.getPrevPage()
                }
            }
        }.onSuccess { page ->
            when (direction) {
                DIRECTION.NEXT -> _onEvent.value = OnEvent.NextPage(page)
                DIRECTION.CURRENT -> _onEvent.value = OnEvent.NextPage(page)
                DIRECTION.PREVIOUS -> _onEvent.value = OnEvent.PrevPage(page)
            }
        }.onFailure { error ->
            showError(error)
        }

        _onEvent.value = OnEvent.HideLoading
        isLoading = false
    }

    private fun showError(ex: Throwable) {
        when (ex) {
            is MarvelApi.BadRequestException -> _onEvent.value =
                OnEvent.ShowError(R.string.page_not_found)
            else -> _onEvent.value = OnEvent.ShowError(R.string.unknown_error)
        }
    }

    sealed class OnEvent {
        object ShowLoading : OnEvent()
        object HideLoading : OnEvent()
        object CleanList : OnEvent()
        data class ShowError(val errorId: Int) : OnEvent()
        data class NextPage(val data: CharactersItemPage) : OnEvent()
        data class PrevPage(val data: CharactersItemPage) : OnEvent()
    }

    enum class DIRECTION { NEXT, CURRENT, PREVIOUS }
}
