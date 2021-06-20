package com.vijay.albums.usecases.repository

import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import retrofit2.http.GET
import java.util.*

interface AlbumsRemoteDataStore {

    @GET("/albums")
    suspend fun loadAlbums(): List<ItemAlbumModel>
}
