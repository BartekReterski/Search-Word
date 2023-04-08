package com.searchdirectly.searchword.domain.data

import android.content.Context
import android.content.SharedPreferences
import com.searchdirectly.searchword.domain.model.WebSites
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SearchWordRepository @Inject constructor(@ApplicationContext val context: Context) : SearchWordInterface{

    override suspend fun getWebSite(websiteName: String): WebSites? {
        val list = mutableListOf<WebSites>()
        list.add(WebSites("Google", "https://www.google.com/search?", "q="))
        list.add(WebSites("Bing", "https://www.bing.com/search?", "q="))
        list.add(WebSites("Yahoo", "https://search.yahoo.com/search?", "p="))
        return list.find { it.siteName == websiteName }
    }

    override fun saveStateUrl(context: Context,url: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("url", url)
        myEdit.apply()
    }

    override fun getSavedUrl(context: Context): String? {
        val sh: SharedPreferences =
            context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        return sh.getString("url", "")
    }
}