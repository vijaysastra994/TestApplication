package com.vijay.albums.usecases.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemAlbumModel::class], version = 1)
abstract class AlbumsDataBase : RoomDatabase() {

    abstract fun albumsDao(): AlbumsLocalDataStore
}