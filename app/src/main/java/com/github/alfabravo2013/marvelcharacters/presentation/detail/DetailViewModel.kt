package com.github.alfabravo2013.marvelcharacters.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alfabravo2013.marvelcharacters.R
import com.github.alfabravo2013.marvelcharacters.domain.detail.DetailUseCase
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail
import com.github.alfabravo2013.marvelcharacters.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val detailUseCase: DetailUseCase
) : ViewModel() {

    private val _onEvent = SingleLiveEvent<OnEvent>()
    val onEvent: SingleLiveEvent<OnEvent> get() = _onEvent

    fun getDetail(characterId: Int) = viewModelScope.launch {
        _onEvent.value = OnEvent.ShowLoading

        runCatching {
            withContext(Dispatchers.IO) {
                detailUseCase.getCharacterById(characterId)
            }
        }.onSuccess { detail ->
            _onEvent.value = OnEvent.ShowDetail(detail)
        }.onFailure { error ->
            showError(error)
        }

        _onEvent.value = OnEvent.HideLoading
    }

    private fun showError(ex: Throwable) {
        when (ex) {
            is MarvelApi.NotFoundException -> _onEvent.value =
                OnEvent.Error(R.string.character_not_found)
            else -> _onEvent.value = OnEvent.Error(R.string.unknown_error)
        }
    }

    sealed class OnEvent {
        object ShowLoading : OnEvent()
        object HideLoading : OnEvent()
        data class ShowDetail(val detail: Detail) : OnEvent()
        data class Error(val errorId: Int) : OnEvent()
    }
}
