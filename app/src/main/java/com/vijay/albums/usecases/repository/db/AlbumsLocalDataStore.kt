package com.vijay.albums.usecases.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface AlbumsLocalDataStore {


    @Query("select * from ItemAlbumModel ORDER BY title ASC")
    fun albums():Flow<List<ItemAlbumModel>>

    @Insert
    suspend fun insertAll(items: List<ItemAlbumModel>)

    @Query("SELECT COUNT(id) FROM ItemAlbumModel")
    fun albumsCount(): Int
}