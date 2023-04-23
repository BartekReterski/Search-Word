package com.searchdirectly.searchword.presentation.usecases.preferences

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import com.searchdirectly.searchword.domain.model.preferences.SharedPreferencesModel
import javax.inject.Inject

class GetSharedPreferencesData @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(): Result<SharedPreferencesModel?> = try {
        Result.success(repository.getSavedUrl())
    } catch (e: java.lang.Exception) {
        Result.failure(e)
    }
}