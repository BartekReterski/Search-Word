package com.searchdirectly.searchword.presentation.usecases.room

import com.searchdirectly.searchword.domain.data.repositories.RoomRepository
import com.searchdirectly.searchword.domain.model.SavedLinks

class RemoveLink(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(savedLinks: SavedLinks): Result<Boolean> = try {
        roomRepository.removeLink(savedLinks)
        Result.success(roomRepository.removeLink(savedLinks))
    } catch (e: java.lang.Exception) {
        Result.failure(e)
    }
}