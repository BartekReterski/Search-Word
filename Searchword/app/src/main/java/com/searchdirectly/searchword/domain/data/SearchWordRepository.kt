package com.searchdirectly.searchword.domain.data

import com.searchdirectly.searchword.domain.model.WebSites
import javax.inject.Inject


class SearchWordRepository @Inject constructor() : SearchWordInterface {

    override suspend fun getWebSite(websiteName: String): WebSites? {
        val list = mutableListOf<WebSites>()
        list.add(WebSites("Google", "https://www.google.com/search?", "q="))
        list.add(WebSites("Bing", "https://www.bing.com/search?", "q="))
        list.add(WebSites("Yahoo", "https://search.yahoo.com/search?", "p="))
        return list.find { it.siteName == websiteName }
    }
}