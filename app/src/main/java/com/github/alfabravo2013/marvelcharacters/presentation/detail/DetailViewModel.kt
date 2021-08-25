package com.github.alfabravo2013.marvelcharacters.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alfabravo2013.marvelcharacters.domain.detail.DetailUseCase
import com.github.alfabravo2013.marvelcharacters.presentation.characters.model.Detail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val useCase: DetailUseCase
) : ViewModel() {

    private val _onEvent = MutableLiveData<OnEvent>()
    val onEvent: LiveData<OnEvent> get() = _onEvent

    fun getDetail(characterId: Int) = viewModelScope.launch {
        _onEvent.value = OnEvent.ShowLoading
        val detail = withContext(Dispatchers.IO) {
            useCase.getCharacterById(characterId)
        }
        _onEvent.value = OnEvent.HideLoading

        if (detail.error.isNotEmpty()) {
            _onEvent.value = OnEvent.Error(detail.error)
        } else {
            _onEvent.value = OnEvent.ShowDetail(detail)
        }
    }

    sealed class OnEvent {
        object ShowLoading : OnEvent()
        object HideLoading : OnEvent()
        data class ShowDetail(val detail: Detail) : OnEvent()
        data class Error(val error: String) : OnEvent()
    }
}
