package com.searchdirectly.searchword.domain.data.interfaces

import com.searchdirectly.searchword.domain.model.room.SavedLinks

interface RoomInterface {

    suspend fun add(savedLinks: SavedLinks): Long
    suspend fun getAll(): List<SavedLinks>
    suspend fun remove(savedLinks: SavedLinks): Int
}