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
        _onEvent.value = OnEvent.BookmarkStatus(detailUseCase.isBookmarked())
    }

    fun onBookmarkClick() = viewModelScope.launch {
        kotlin.runCatching {
            detailUseCase.switchBookmarkedStatus()
        }.onSuccess { bookmarked ->
            _onEvent.value = OnEvent.BookmarkStatus(bookmarked)
            val messageId = if (bookmarked) {
                R.string.detail_bookmark_added
            } else {
                R.string.detail_bookmark_removed
            }
            _onEvent.value = OnEvent.Message(messageId)
        }.onFailure {
            _onEvent.value = OnEvent.Message(R.string.detail_bookmark_error)
        }
    }

    private fun showError(ex: Throwable) {
        when (ex) {
            is MarvelApi.NotFoundException -> _onEvent.value =
                OnEvent.Message(R.string.character_not_found)
            else -> _onEvent.value = OnEvent.Message(R.string.unknown_error)
        }
    }

    sealed class OnEvent {
        object ShowLoading : OnEvent()
        object HideLoading : OnEvent()
        data class BookmarkStatus(val bookmarked: Boolean) : OnEvent()
        data class ShowDetail(val detail: Detail) : OnEvent()
        data class Message(val messageId: Int) : OnEvent()
    }
}
