package com.searchdirectly.searchword.domain.data

import com.searchdirectly.searchword.domain.model.WebSites

interface SearchWordInterface {

//    suspend fun getAll(): List<WebSites>
    suspend fun getWebSite(websiteName: String): WebSites?
}