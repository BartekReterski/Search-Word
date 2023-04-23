package com.searchdirectly.searchword.domain.data.repositories

import android.content.Context
import com.searchdirectly.searchword.domain.data.interfaces.RoomInterface
import com.searchdirectly.searchword.domain.db.DatabaseService
import com.searchdirectly.searchword.domain.db.SavedLinksEntity
import com.searchdirectly.searchword.domain.model.room.SavedLinks

class RoomDataSource(context: Context) : RoomInterface {
    private val savedLinksDao = DatabaseService.getInstance(context).savedLinksDao()

    override suspend fun add(savedLinks: SavedLinks): Long = savedLinksDao.addLinkEntity(
        SavedLinksEntity.fromSavedLinks(savedLinks)
    )

    override suspend fun getAll(): List<SavedLinks> =
        savedLinksDao.getAllLinksEntities().map { it.toSavedLinks() }

    override suspend fun remove(savedLinks: SavedLinks): Int =
        savedLinksDao.deleteLinkEntity(savedLinks.hyperLink)
}