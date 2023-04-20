package com.searchdirectly.searchword.domain.data.interfaces

import com.searchdirectly.searchword.domain.model.WebSites

interface SearchWordInterface {

    suspend fun getWebSite(websiteName: String): WebSites?
    suspend fun saveStateUrl(url: String): Boolean
    suspend fun getSavedUrl(): String?
    suspend fun isNetworkAvailable(): Boolean

}