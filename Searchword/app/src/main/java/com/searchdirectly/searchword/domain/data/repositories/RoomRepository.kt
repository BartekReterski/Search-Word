package com.searchdirectly.searchword.domain.data.repositories

import com.searchdirectly.searchword.domain.data.interfaces.RoomInterface
import com.searchdirectly.searchword.domain.model.room.SavedLinks

class RoomRepository(private val roomInterface: RoomInterface) {
    suspend fun addWebLink(savedLinks: SavedLinks): Boolean = roomInterface.add(savedLinks) != 0L
    suspend fun getAllLinks() = roomInterface.getAll()
    suspend fun removeLink(savedLinks: SavedLinks): Boolean = roomInterface.remove(savedLinks) == 1
}