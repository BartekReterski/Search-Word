package com.searchdirectly.searchword.presentation.usecases.room

import com.searchdirectly.searchword.domain.data.repositories.RoomRepository
import com.searchdirectly.searchword.domain.model.SavedLinks
import javax.inject.Inject

class AddWebLink @Inject constructor(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(savedLinks: SavedLinks): Boolean =
        roomRepository.addWebLink(savedLinks)
}