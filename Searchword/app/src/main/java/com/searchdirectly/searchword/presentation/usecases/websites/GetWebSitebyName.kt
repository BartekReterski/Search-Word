package com.searchdirectly.searchword.presentation.usecases.websites

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import com.searchdirectly.searchword.domain.model.WebSites
import javax.inject.Inject

class GetWebSiteByName @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(webSiteName: String): Result<WebSites?> = try {
        Result.success(repository.getWebSite(webSiteName))
    } catch (e: java.lang.Exception) {
        Result.failure(e)
    }
}