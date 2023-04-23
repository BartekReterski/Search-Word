package com.searchdirectly.searchword.presentation.uistates.websites

import com.searchdirectly.searchword.domain.model.websites.WebSites

data class WebSitesUiState(
    val listState: WebState = WebState.Empty,
)

sealed class WebState {
    data class Success(val webSite: WebSites?) : WebState()
    data class Error(val message: String) : WebState()
    data class NoInternetConnection(val message: String): WebState()
    object Loading : WebState()
    object Empty : WebState()
}