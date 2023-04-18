package com.searchdirectly.searchword.presentation.viewmodels.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchdirectly.searchword.domain.model.SavedLinks
import com.searchdirectly.searchword.presentation.uistates.room.RoomLinkUiState
import com.searchdirectly.searchword.presentation.uistates.room.RoomLinksListState
import com.searchdirectly.searchword.presentation.uistates.room.RoomLinksListUiState
import com.searchdirectly.searchword.presentation.usecases.room.AddWebLink
import com.searchdirectly.searchword.presentation.usecases.room.GetAllWebLinks
import com.searchdirectly.searchword.presentation.usecases.room.RemoveLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLinksViewModel @Inject constructor(
    private val getAllWebLinks: GetAllWebLinks,
    private val removeLink: RemoveLink,
    private val addWebLink: AddWebLink
) : ViewModel() {

    private val _roomLinkUiState = MutableStateFlow(RoomLinkUiState())
    val roomLinkUiState: StateFlow<RoomLinkUiState> = _roomLinkUiState

    private val _roomLinksListUiState = MutableStateFlow(RoomLinksListUiState())
    val roomLinksListUiState: StateFlow<RoomLinksListUiState> = _roomLinksListUiState

    private var getAllWebLinksJob: Job? = null
    private var addWebLinkJob: Job? = null
    private var removeLinkJob: Job? = null

    fun getSavedWebLinks() {
        getAllWebLinksJob?.cancel()
        getAllWebLinksJob = viewModelScope.launch {
            _roomLinksListUiState.update {
                it.copy(listLinksState = RoomLinksListState.Loading)
            }
            val savedLinks = getAllWebLinks.invoke()
            if (savedLinks.isSuccess) {
                _roomLinksListUiState.update {
                    it.copy(listLinksState = RoomLinksListState.Success(savedLinks.getOrThrow()))
                }

            } else {
                _roomLinksListUiState.update {
                    it.copy(listLinksState = RoomLinksListState.Error("There is a problem with getting list of saved hyperlinks"))
                }
            }
        }
    }

    fun saveWebLink(savedLinks: SavedLinks) {
        addWebLinkJob?.cancel()
        addWebLinkJob = viewModelScope.launch {
            if (addWebLink.invoke(savedLinks)) {
                _roomLinkUiState.update {
                    it.copy(showedAddedMessage = true)
                }
            }
        }
    }

    fun removeWebLink(savedLinks: SavedLinks) {
        removeLinkJob?.cancel()
        removeLinkJob = viewModelScope.launch {
            if (removeLink.invoke(savedLinks)) {
                _roomLinkUiState.update {
                    it.copy(showedRemovedMessage = true)
                }
                getSavedWebLinks()
            }
        }
    }

    fun addedMessageInfo() {
        _roomLinkUiState.update {
            it.copy(showedAddedMessage = false)
        }
    }

    fun removedMessageInfo() {
        _roomLinkUiState.update {
            it.copy(showedRemovedMessage = false)
        }
    }

}