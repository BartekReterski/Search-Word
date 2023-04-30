package com.searchdirectly.searchword.presentation.usecases.savestate

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import com.searchdirectly.searchword.domain.model.savestate.SaveState
import javax.inject.Inject

class GetSaveStateData @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(): Result<SaveState> = try {
        Result.success(repository.getSavedUrl())
    } catch (e: java.lang.Exception) {
        Result.failure(e)
    }
}