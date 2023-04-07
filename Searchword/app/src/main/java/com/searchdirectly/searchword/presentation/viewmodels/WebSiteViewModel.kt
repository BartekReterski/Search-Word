package com.searchdirectly.searchword.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchdirectly.searchword.presentation.uistates.WebSitesUiState
import com.searchdirectly.searchword.presentation.uistates.WebState
import com.searchdirectly.searchword.presentation.usecases.GetWebSiteByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebSiteViewModel @Inject constructor(private val getWebSiteByName: GetWebSiteByName) :
    ViewModel() {

    private val _webSitesUiState = MutableStateFlow(WebSitesUiState())
    val webSitesUiState: StateFlow<WebSitesUiState> = _webSitesUiState

    private var getWebSiteByNameJob: Job? = null

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

}