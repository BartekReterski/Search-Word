package com.searchdirectly.searchword.presentation.uistates.preferences

import com.searchdirectly.searchword.domain.model.preferences.SharedPreferencesModel

data class SharedPreferencesUiState(
    val sharedPreferenceState: SharedPreferencesState = SharedPreferencesState.Empty,
    val showedSharedPreferencesAddedMessage: Boolean = false
)

sealed class SharedPreferencesState {
    data class Success(val sharedPreferencesModel: SharedPreferencesModel?) : SharedPreferencesState()
    data class Error(val message: String) : SharedPreferencesState()
    object Loading : SharedPreferencesState()
    object Empty : SharedPreferencesState()
}
