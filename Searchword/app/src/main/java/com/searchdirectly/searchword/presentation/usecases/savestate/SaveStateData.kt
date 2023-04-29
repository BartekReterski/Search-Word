package com.searchdirectly.searchword.presentation.usecases.savestate

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import com.searchdirectly.searchword.domain.model.savestate.SaveState
import javax.inject.Inject

class SaveStateData @Inject constructor(private val repository: SearchWordRepository) {

    suspend operator fun invoke(saveState: SaveState): Boolean =
        repository.saveStateUrl(saveState.queryValue, saveState.hyperLink)
}