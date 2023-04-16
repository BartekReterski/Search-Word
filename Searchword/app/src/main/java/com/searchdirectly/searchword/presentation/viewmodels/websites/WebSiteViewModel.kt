package com.searchdirectly.searchword.presentation.viewmodels.websites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchdirectly.searchword.presentation.uistates.preferences.SharedPreferencesState
import com.searchdirectly.searchword.presentation.uistates.preferences.SharedPreferencesUiState
import com.searchdirectly.searchword.presentation.uistates.websites.WebSitesUiState
import com.searchdirectly.searchword.presentation.uistates.websites.WebState
import com.searchdirectly.searchword.presentation.usecases.preferences.GetSavedUrl
import com.searchdirectly.searchword.presentation.usecases.preferences.SaveUrl
import com.searchdirectly.searchword.presentation.usecases.websites.GetWebSiteByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebSiteViewModel @Inject constructor(
    private val getWebSiteByName: GetWebSiteByName,
    private val getSavedUrl: GetSavedUrl,
    private val saveUrl: SaveUrl
) :
    ViewModel() {

    private val _webSitesUiState = MutableStateFlow(WebSitesUiState())
    val webSitesUiState: StateFlow<WebSitesUiState> = _webSitesUiState

    private val _sharedPreferencesUiState = MutableStateFlow(SharedPreferencesUiState())
    val sharedPreferencesUiState: StateFlow<SharedPreferencesUiState> = _sharedPreferencesUiState

    private var getWebSiteByNameJob: Job? = null
    private var getSavedSharedPreferencesUrlJob: Job? = null
    private var saveSharedPreferencesUrlJob: Job? = null

    fun getWebsiteDataByName(webSiteName: String) {
        getWebSiteByNameJob?.cancel()
        getWebSiteByNameJob = viewModelScope.launch {
            _webSitesUiState.update {
                it.copy(listState = WebState.Loading)
            }
            val website = getWebSiteByName.invoke(webSiteName)
            if (website.isSuccess) {
                _webSitesUiState.update {
                    it.copy(listState = WebState.Success(website.getOrThrow()))
                }

            } else {
                _webSitesUiState.update {
                    it.copy(listState = WebState.Error("There is a problem with pass WebSiteName"))
                }
            }
        }
    }

    fun getSavedSharedPreferencesUrl() {
        getSavedSharedPreferencesUrlJob?.cancel()
        getSavedSharedPreferencesUrlJob = viewModelScope.launch {
            _sharedPreferencesUiState.update {
                it.copy(sharedPreferenceState = SharedPreferencesState.Loading)
            }
            val getSavedUrl = getSavedUrl.invoke()
            if (getSavedUrl.isSuccess) {
                _sharedPreferencesUiState.update {
                    it.copy(sharedPreferenceState = SharedPreferencesState.Success(getSavedUrl.getOrThrow()))
                }

            } else {
                _sharedPreferencesUiState.update {
                    it.copy(sharedPreferenceState = SharedPreferencesState.Error("There is a problem with getting saved Url"))
                }
            }
        }
    }

    fun saveSharedPreferencesUrl(url: String) {
        saveSharedPreferencesUrlJob?.cancel()
        saveSharedPreferencesUrlJob = viewModelScope.launch {
            if (saveUrl.invoke(url)) {
                _sharedPreferencesUiState.update {
                    it.copy(showedSharedPreferencesAddedMessage = true)
                }
            }

        }
    }

    fun addedMessageInfo() {
        _sharedPreferencesUiState.update {
            it.copy(showedSharedPreferencesAddedMessage = false)
        }
    }
}