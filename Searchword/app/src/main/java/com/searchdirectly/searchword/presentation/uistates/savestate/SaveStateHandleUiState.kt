package com.searchdirectly.searchword.presentation.uistates.savestate

import com.searchdirectly.searchword.domain.model.savestate.SaveState

data class SaveStateHandleUiState(
    val saveStateHandle: SaveStateHandle = SaveStateHandle.Empty,
    val showedSaveStateHandleAddedMessage: Boolean = false
)

sealed class SaveStateHandle {
    data class Success(val saveState: SaveState) : SaveStateHandle()
    data class Error(val message: String) : SaveStateHandle()
    object Loading : SaveStateHandle()
    object Empty : SaveStateHandle()
}
