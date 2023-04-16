package com.searchdirectly.searchword.presentation.usecases.room

import com.searchdirectly.searchword.domain.data.repositories.RoomRepository
import com.searchdirectly.searchword.domain.model.SavedLinks

class AddWebLink(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(savedLinks: SavedLinks): Result<Boolean> = try {
        roomRepository.addWebLink(savedLinks)
        Result.success(roomRepository.addWebLink(savedLinks))
    } catch (e: java.lang.Exception) {
        Result.failure(e)
    }
}