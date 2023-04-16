package com.searchdirectly.searchword.domain.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface SavedLinksDao {
    //this function is also for update entity, because when is conflict(the entity is the same), we simply replace by the new one
    @Insert(onConflict = REPLACE)
    suspend fun addLinkEntity(savedLinksEntity: SavedLinksEntity): Long

    @Query("SELECT * FROM SAVED_LINKS_TABLE")
    suspend fun getAllLinksEntities(): List<SavedLinksEntity>

    @Delete
    suspend fun deleteLinkEntity(savedLinksEntity: SavedLinksEntity): Int


}