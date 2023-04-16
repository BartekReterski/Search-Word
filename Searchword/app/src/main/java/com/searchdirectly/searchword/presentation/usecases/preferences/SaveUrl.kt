package com.searchdirectly.searchword.presentation.usecases.preferences

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import javax.inject.Inject

class SaveUrl @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(url: String): Boolean = repository.saveStateUrl(url)
}