package com.searchdirectly.searchword.domain.data.interfaces

import com.searchdirectly.searchword.domain.model.savestate.SaveState
import com.searchdirectly.searchword.domain.model.websites.WebSites

interface SearchWordInterface {

    suspend fun getWebSite(websiteName: String): WebSites?
    suspend fun saveStateUrl(link: String?, query: String?): Boolean
    suspend fun getSavedUrl(): SaveState
}