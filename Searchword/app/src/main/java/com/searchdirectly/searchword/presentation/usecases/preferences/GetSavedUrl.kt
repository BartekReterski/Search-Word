package com.searchdirectly.searchword.presentation.usecases.preferences

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import javax.inject.Inject

class GetSavedUrl @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(): Result<String?> = try {
        repository.getSavedUrl()
        Result.success(repository.getSavedUrl())
    } catch (e: java.lang.Exception) {
        Result.failure(e)
    }
}