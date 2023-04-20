package com.searchdirectly.searchword.domain.data.repositories

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.domain.data.interfaces.SearchWordInterface
import com.searchdirectly.searchword.domain.model.WebSites
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchWordRepository @Inject constructor(@ApplicationContext val context: Context) :
    SearchWordInterface {

    override suspend fun getWebSite(websiteName: String): WebSites? {
        val list = mutableListOf<WebSites>()
        list.add(
            WebSites(
                context.getString(R.string.google_name),
                context.getString(R.string.google_value),
                context.getString(
                    R.string.google_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.bing_name),
                context.getString(R.string.bing_value),
                context.getString(
                    R.string.bing_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.yahoo_name),
                context.getString(R.string.yahoo_value),
                context.getString(
                    R.string.yahoo_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.duck_duck_go),
                context.getString(R.string.duck_duck_go_value),
                context.getString(
                    R.string.duck_duck_go_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.yandex_name),
                context.getString(R.string.yandex_value),
                context.getString(
                    R.string.yandex_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.youtube_name),
                context.getString(R.string.youtube_value),
                context.getString(
                    R.string.youtube_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.twitter_name),
                context.getString(R.string.twitter_value),
                context.getString(
                    R.string.twitter_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.instagram_name),
                context.getString(R.string.instagram_value),
                ""
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.reddit_name),
                context.getString(R.string.reddit_value),
                context.getString(
                    R.string.reddit_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.wikipedia_name),
                context.getString(R.string.wikipedia_value),
                ""
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.amazon_name),
                context.getString(R.string.amazon_value),
                context.getString(
                    R.string.amazon_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.ebay_name),
                context.getString(R.string.ebay_value),
                context.getString(
                    R.string.ebay_endpoint
                )
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.pinterest_name),
                context.getString(R.string.pinterest_value),
                context.getString(
                    R.string.pinterest_endpoint
                )
            )
        )

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

    override suspend fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}