package com.vijay.albums.usecases.repository.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemAlbumModel(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String?
)