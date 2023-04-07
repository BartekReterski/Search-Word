package com.searchdirectly.searchword.presentation.usecases

import com.searchdirectly.searchword.domain.data.SearchWordRepository
import com.searchdirectly.searchword.domain.model.WebSites
import javax.inject.Inject

class GetWebSiteByName @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(webSiteName: String): Result<WebSites?> = try {
        repository.getWebSite(webSiteName)
        Result.success(repository.getWebSite(webSiteName))
    } catch (e: java.lang.Exception) {
        Result.failure(e)
    }
}