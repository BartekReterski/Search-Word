package com.searchdirectly.searchword.domain.data

import android.content.Context
import android.content.SharedPreferences
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.domain.model.WebSites
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchWordRepository @Inject constructor(@ApplicationContext val context: Context) :
    SearchWordInterface {

    override suspend fun getWebSite(websiteName: String): WebSites? {
        val list = mutableListOf<WebSites>()
        list.add(WebSites("Google", "https://www.google.com/search?", "q="))
        list.add(WebSites("Bing", "https://www.bing.com/search?", "q="))
        list.add(WebSites("Yahoo", "https://search.yahoo.com/search?", "p="))
        list.add(WebSites("Yandex", "https://yandex.com/search/?", "text="))
        list.add(WebSites("Youtube", "https://www.youtube.com/results?", "search_query="))
        list.add(WebSites("Twitter", "https://twitter.com/search?lang=pl&", "q="))
        list.add(WebSites("Instagram", "https://www.instagram.com/explore/tags/", ""))
        //list.add(WebSites("Tiktok", "https://www.tiktok.com/search?", "q="))
        list.add(WebSites("Reddit", "https://www.reddit.com/search/?", "q="))
        list.add(WebSites("Wikipedia", "https://en.m.wikipedia.org/wiki/", ""))
        list.add(WebSites("Amazon", "https://www.amazon.pl/s?", "k="))
        list.add(WebSites("Ebay", "https://www.ebay.pl/sch/i.html?_", "nkw="))
        list.add(WebSites("Pinterest", "https://pl.pinterest.com/search/pins/?", "q="))
        //list.add(WebSites("Dictionary", "https://search.yahoo.com/search?", "p="))

        return list.find { it.siteName == websiteName }
    }

    override suspend fun saveStateUrl(url: String): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                context.getString(R.string.Shared_pref_id),
                Context.MODE_PRIVATE
            )
        val myEdit = sharedPreferences.edit()
        myEdit.putString("url", url)
        myEdit.apply()
        return true
    }

    override suspend fun getSavedUrl(): String? {
        val sh: SharedPreferences =
            context.getSharedPreferences(
                context.getString(R.string.Shared_pref_id),
                Context.MODE_PRIVATE
            )
        return sh.getString("url", "")
    }
}