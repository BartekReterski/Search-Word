package com.searchdirectly.searchword.presentation.usecases.room

import com.searchdirectly.searchword.domain.data.repositories.RoomRepository
import com.searchdirectly.searchword.domain.model.room.SavedLinks
import javax.inject.Inject

class RemoveLink @Inject constructor(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(savedLinks: SavedLinks): Boolean =
        roomRepository.removeLink(savedLinks)
}