package com.searchdirectly.searchword.presentation.usecases.preferences

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import com.searchdirectly.searchword.domain.model.preferences.SharedPreferencesModel
import javax.inject.Inject

class SaveSharedPreferencesData @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(sharedPreferencesModel: SharedPreferencesModel): Boolean = repository.saveStateUrl(sharedPreferencesModel)
}