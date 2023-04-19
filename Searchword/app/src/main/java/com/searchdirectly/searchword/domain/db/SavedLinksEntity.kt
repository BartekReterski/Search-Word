package com.searchdirectly.searchword.domain.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.searchdirectly.searchword.domain.model.SavedLinks

@Entity(tableName = "saved_links_table", indices = [Index(value = ["hyperLink"], unique = true)])
data class SavedLinksEntity(

    var title: String,
    var hyperLink: String,

    //column info annotation is for changing time immediately after update or creation
    @ColumnInfo(name = "creation_time")
    var creationTime: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) {
    companion object {
        //create SavedLinksEntity for Room database based on SavedLinks model from data model
        fun fromSavedLinks(savedLinks: SavedLinks) =
            SavedLinksEntity(savedLinks.title, savedLinks.hyperLink, savedLinks.creationTime)
    }

    //create SavedLinks model based on SavedLinksEntity
    fun toSavedLinks() = SavedLinks(title, hyperLink, creationTime)
}