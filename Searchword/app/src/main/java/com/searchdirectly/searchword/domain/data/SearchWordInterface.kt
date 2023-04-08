package com.searchdirectly.searchword.domain.data

import android.content.Context
import com.searchdirectly.searchword.domain.model.WebSites

interface SearchWordInterface {

    suspend fun getWebSite(websiteName: String): WebSites?
    fun saveStateUrl(context: Context,url: String)
    fun getSavedUrl(context: Context): String?
}