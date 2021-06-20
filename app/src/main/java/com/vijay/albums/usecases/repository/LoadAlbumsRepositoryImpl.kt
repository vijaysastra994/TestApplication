package com.vijay.albums.usecases.repository

import com.vijay.albums.usecases.repository.db.AlbumsLocalDataStore
import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class LoadAlbumsRepositoryImpl @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val albumsDao: AlbumsLocalDataStore,
    private val albumsRemoteDataStore: AlbumsRemoteDataStore
) : LoadAlbumsRepository {

    override val netWorkFailureStateFlow: MutableStateFlow<NetworkError?> = MutableStateFlow(null)

    override fun albums(coroutineScope: CoroutineScope): Flow<List<ItemAlbumModel>> {
        coroutineScope.launch(coroutineDispatcher) {
            val result = albumsRemoteDataStore.runCatching {
                albumsDao.insertAll(loadAlbums())
            }
            if (result.isFailure && albumsDao.albumsCount() == 0) {
                netWorkFailureStateFlow.emit(NetworkError(result.exceptionOrNull()))
            }
        }

        return albumsDao.albums()
    }
}