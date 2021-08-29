package com.github.alfabravo2013.marvelcharacters.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.domain.characters.CharactersUseCase
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersItemPage
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.CharactersScreenState
import com.github.alfabravo2013.marvelcharacters.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(private val charactersUseCase: CharactersUseCase) : ViewModel() {
    private var isLoading = false

    private val _onEvent = SingleLiveEvent<OnEvent>()
    val onEvent: SingleLiveEvent<OnEvent> get() = _onEvent

    private val _screenState = MutableLiveData<CharactersScreenState>()
    val screenState: LiveData<CharactersScreenState> get() = _screenState

    init {
        getCharactersPage(PAGE.FIRST)
        _screenState.value = CharactersScreenState()
    }

    fun updateQueryText(text: String?): Boolean {
        // TODO: 29.08.2021 store the SearchView state to avoid collapsed and empty search text
        //  while a search filter is applied
        return charactersUseCase.updateQueryText(text)
    }

    fun onToggleUniqueNamesFilter() {
        // TODO: 29.08.2021 add this feature
        _onEvent.value = OnEvent.ShowError(R.string.not_implemented)
    }

    fun onToggleWithImageFilter() {
        val isChecked = _screenState.value?.hasImageFilterOn?.not() ?: true
        _screenState.value = _screenState.value?.copy(hasImageFilterOn = isChecked)

        if (isChecked) {
            charactersUseCase.addWithImageFilter()
        } else {
            charactersUseCase.removeWithImageFilter()
        }

        getCharactersPage(PAGE.FIRST)
    }

    fun onToggleWithDescriptionFilter() {
        val isChecked = _screenState.value?.hasDescriptionFilterOn?.not() ?: true
        _screenState.value = _screenState.value?.copy(hasDescriptionFilterOn = isChecked)

        if (isChecked) {
            charactersUseCase.addWithDescriptionFilter()
        } else {
            charactersUseCase.removeWithDescriptionFilter()
        }

        getCharactersPage(PAGE.FIRST)
    }

    fun getCharactersPage(requestedPage: PAGE) = viewModelScope.launch {
        if (isLoading) {
            return@launch
        }

        isLoading = true
        _onEvent.value = OnEvent.ShowLoading

        runCatching {
            withContext(Dispatchers.IO) {
                when (requestedPage) {
                    PAGE.NEXT -> charactersUseCase.getNextPage()
                    PAGE.FIRST -> {
                        _onEvent.postValue(OnEvent.CleanList)
                        charactersUseCase.getFirstPage()
                    }
                    PAGE.PREVIOUS -> charactersUseCase.getPrevPage()
                }
            }
        }.onSuccess { fetchedPage ->
            when (requestedPage) {
                PAGE.NEXT -> _onEvent.value = OnEvent.NextPage(fetchedPage)
                PAGE.FIRST -> _onEvent.value = OnEvent.NextPage(fetchedPage)
                PAGE.PREVIOUS -> _onEvent.value = OnEvent.PrevPage(fetchedPage)
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
            else -> {
                _onEvent.value = OnEvent.ShowError(R.string.unknown_error)
            }
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

    enum class PAGE { NEXT, FIRST, PREVIOUS }
}
