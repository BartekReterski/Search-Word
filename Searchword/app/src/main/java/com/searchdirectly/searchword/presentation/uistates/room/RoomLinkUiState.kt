package com.searchdirectly.searchword.presentation.uistates.room

import com.searchdirectly.searchword.domain.model.room.SavedLinks

data class RoomLinkUiState(
    val roomLinkState: RoomLinkState = RoomLinkState.Empty,
    val showedAddedMessage: Boolean = false,
    val showedRemovedMessage: Boolean = false
)

sealed class RoomLinkState {
    data class Success(val idLink: SavedLinks) : RoomLinkState()
    data class Error(val message: String) : RoomLinkState()
    object Loading : RoomLinkState()
    object Empty : RoomLinkState()
}