package com.searchdirectly.searchword.presentation.uistates.room

import com.searchdirectly.searchword.domain.model.room.SavedLinks

data class RoomLinksListUiState(
    val listLinksState: RoomLinksListState = RoomLinksListState.Empty,
    val showedRemoveMessage: Boolean = false
)

sealed class RoomLinksListState {
    data class Success(val linkList: List<SavedLinks>) : RoomLinksListState()
    data class Error(val message: String) : RoomLinksListState()
    object Loading : RoomLinksListState()
    object Empty : RoomLinksListState()
}