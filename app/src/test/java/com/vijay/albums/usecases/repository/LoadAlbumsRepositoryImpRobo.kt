package com.vijay.albums.usecases.repository

import com.vijay.albums.usecases.repository.db.AlbumsLocalDataStore
import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertEquals

class LoadAlbumsRepositoryImpRobo(
    val coroutineScope: CoroutineScope,
    coroutineDispatcher: CoroutineDispatcher,
    itemAlbumModelsLocalFlow: MutableStateFlow<List<ItemAlbumModel>>,
    albumsCount: Int = 0,
    error: Exception? = null,
    vararg itemAlbumModelsRemotes: List<ItemAlbumModel>
) {

    private val albumsLocalDataStore: AlbumsLocalDataStore = mock()
    private val albumsRemoteDataStore: AlbumsRemoteDataStore = mock()
    private val repositoryImpl: LoadAlbumsRepositoryImpl
    private val networkErrors = mutableListOf<NetworkError?>()
    private val networkFailuresJob: Job

    init {
        whenever(albumsLocalDataStore.albums()).thenReturn(itemAlbumModelsLocalFlow)
        albumsRemoteDataStore.stub {
            val stubbing = onBlocking { loadAlbums() }
            when {
                itemAlbumModelsRemotes.isEmpty() -> {
                    stubbing.thenThrow(error)
                    whenever(albumsLocalDataStore.albumsCount()) doReturn albumsCount
                }
                itemAlbumModelsRemotes.size == 1 -> {
                    stubbing doReturn itemAlbumModelsRemotes[0]
                }
                else -> {
                    throw IllegalStateException("wrong test arguments itemAlbumModelsRemotes size shouldn't be more than 1")
                }
            }
        }
        repositoryImpl = LoadAlbumsRepositoryImpl(
            coroutineDispatcher,
            albumsLocalDataStore,
            albumsRemoteDataStore
        )

        networkFailuresJob = coroutineScope.launch {
            repositoryImpl.netWorkFailureStateFlow.collect {
                networkErrors.add(it)
            }
        }
    }

    fun albums(): LoadAlbumsRepositoryImpRobo {
        repositoryImpl.albums(coroutineScope)

        return this
    }

    fun verifyLoadFromRemoteDataStore(): LoadAlbumsRepositoryImpRobo {
        verifyBlocking(albumsRemoteDataStore) {
            loadAlbums()
            times(1)
        }

        return this
    }

    fun verifyInsertItemsToLocalDataStore(vararg itemAlbumModels: List<ItemAlbumModel>): LoadAlbumsRepositoryImpRobo {
        val argumentCaptor = argumentCaptor<List<ItemAlbumModel>>()
        if (itemAlbumModels.isNotEmpty()) {
            verifyBlocking(albumsLocalDataStore) {
                times(itemAlbumModels.size)
                insertAll(argumentCaptor.capture())
            }
        }
        assertEquals(itemAlbumModels.toList(), argumentCaptor.allValues)

        return this
    }

    fun verifyNetworkError(vararg networkError: NetworkError?): LoadAlbumsRepositoryImpRobo {
        assertEquals(networkError.toList(), networkErrors)
        networkFailuresJob.cancel()
        return this
    }

}