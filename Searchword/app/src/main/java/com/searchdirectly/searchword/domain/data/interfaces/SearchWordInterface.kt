package com.searchdirectly.searchword.domain.data.interfaces

import com.searchdirectly.searchword.domain.model.preferences.SharedPreferencesModel
import com.searchdirectly.searchword.domain.model.websites.WebSites

interface SearchWordInterface {

    suspend fun getWebSite(websiteName: String): WebSites?
    suspend fun saveStateUrl(sharedPreferencesModel: SharedPreferencesModel): Boolean
    suspend fun getSavedUrl(): SharedPreferencesModel
}