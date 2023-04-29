package com.searchdirectly.searchword.domain.data.repositories

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.domain.data.interfaces.SearchWordInterface
import com.searchdirectly.searchword.domain.model.savestate.SaveState
import com.searchdirectly.searchword.domain.model.websites.WebSites
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchWordRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val state: SavedStateHandle
) :
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
                context.getString(R.string.spotify_name),
                context.getString(R.string.spotify_value),
                ""
            )
        )
        list.add(
            WebSites(
                context.getString(R.string.google_maps_name),
                context.getString(R.string.google_maps_value),
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

    override suspend fun saveStateUrl(link: String?, query: String?): Boolean {
        state["Url"] = link
        state["Query"] = query
        return true
    }

    override suspend fun getSavedUrl(): SaveState {
        return SaveState(
            state.get<SaveState>("Query").toString(),
            state.get<SaveState>("Url").toString()
        )
    }
}