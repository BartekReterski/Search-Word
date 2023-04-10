package com.searchdirectly.searchword.presentation.uistates.preferences

data class SharedPreferencesUiState(
    val sharedPreferenceState: SharedPreferencesState = SharedPreferencesState.Empty,
    val showedSharedPreferencesAddedMessage: Boolean = false
)

sealed class SharedPreferencesState {
    data class Success(val url: String?) : SharedPreferencesState()
    data class Error(val message: String) : SharedPreferencesState()
    object Loading : SharedPreferencesState()
    object Empty : SharedPreferencesState()
}
