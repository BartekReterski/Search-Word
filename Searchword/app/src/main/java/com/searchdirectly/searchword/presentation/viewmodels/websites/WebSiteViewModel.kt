package com.searchdirectly.searchword.presentation.viewmodels.websites

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.searchdirectly.searchword.domain.model.savestate.SaveState
import com.searchdirectly.searchword.presentation.uistates.savestate.SaveStateHandle
import com.searchdirectly.searchword.presentation.uistates.savestate.SaveStateHandleUiState
import com.searchdirectly.searchword.presentation.uistates.websites.WebSitesUiState
import com.searchdirectly.searchword.presentation.uistates.websites.WebState
import com.searchdirectly.searchword.presentation.usecases.savestate.GetSaveStateData
import com.searchdirectly.searchword.presentation.usecases.savestate.SaveStateData
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
    private val saveStateData: SaveStateData,
    application: Application,
    private val getWebSiteByName: GetWebSiteByName,
    private val getSaveStateData: GetSaveStateData,
) :
    AndroidViewModel(application = application) {

    private val context
        get() = getApplication<Application>()

    private val _webSitesUiState = MutableStateFlow(WebSitesUiState())
    val webSitesUiState: StateFlow<WebSitesUiState> = _webSitesUiState

    private val _saveStateHandleUiState = MutableStateFlow(SaveStateHandleUiState())
    val saveStateHandleUiState: StateFlow<SaveStateHandleUiState> = _saveStateHandleUiState

    private var getWebSiteByNameJob: Job? = null

    private var getSavedStateJob: Job? = null
    private var saveSavedStateJob: Job? = null

    fun getWebsiteDataByName(webSiteName: String) {
        if (isNetworkAvailable(context)) {
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
        } else {
            _webSitesUiState.update {
                it.copy(listState = WebState.NoInternetConnection("No internet connection"))
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    fun getSavedState() {
        getSavedStateJob?.cancel()
        getSavedStateJob = viewModelScope.launch {
            _saveStateHandleUiState.update {
                it.copy(saveStateHandle = SaveStateHandle.Loading)
            }
            val getSavedStateData = getSaveStateData.invoke()
            if (getSavedStateData.isSuccess) {
                _saveStateHandleUiState.update {
                    it.copy(
                        saveStateHandle = SaveStateHandle.Success(
                            getSavedStateData.getOrThrow()
                        )
                    )
                }

            } else {
                _saveStateHandleUiState.update {
                    it.copy(saveStateHandle = SaveStateHandle.Error("There is a problem with getting saved Url"))
                }
            }
        }
    }

    fun saveSavedState(saveState: SaveState) {
        saveSavedStateJob?.cancel()
        saveSavedStateJob = viewModelScope.launch {
            if (saveStateData.invoke(saveState)) {
                _saveStateHandleUiState.update {
                    it.copy(showedSaveStateHandleAddedMessage = true)
                }
            }

        }
    }

    fun addedSavedSateInfo() {
        _saveStateHandleUiState.update {
            it.copy(showedSaveStateHandleAddedMessage = false)
        }
    }
}