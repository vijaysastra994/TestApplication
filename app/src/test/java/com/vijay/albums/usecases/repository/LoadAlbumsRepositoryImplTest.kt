package com.vijay.albums.usecases.repository

import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import com.vijay.albums.utils.CoroutineTestExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.ExperimentalTime

class LoadAlbumsRepositoryImplTest {

    @ExperimentalCoroutinesApi
    @RegisterExtension
    @JvmField
    val coroutineTestExtension = CoroutineTestExtension()

    private val itemAlbumModel = ItemAlbumModel(1, "title")
    private val itemAlbumModels = listOf(itemAlbumModel)
    private val itemAlbumModelsFlow = MutableStateFlow(itemAlbumModels)
    private val error = IllegalStateException("something went wrong :O")

    @ExperimentalCoroutinesApi
    @Test
    fun `run success`() = coroutineTestExtension.testDispatcher.runBlockingTest {
        val localAvailableAlbumsCount = 0
        LoadAlbumsRepositoryImpRobo(
            this,
            coroutineTestExtension.testDispatcher,
            itemAlbumModelsFlow,
            localAvailableAlbumsCount,
            error,
            itemAlbumModels
        )
            .albums()
            .verifyLoadFromRemoteDataStore()
            .verifyInsertItemsToLocalDataStore(itemAlbumModels)
            .verifyNetworkError(null)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun `run failure no Items available in local`() =
        coroutineTestExtension.testDispatcher.runBlockingTest {
            val localAvailableAlbumsCount = 0

            LoadAlbumsRepositoryImpRobo(
                this,
                coroutineTestExtension.testDispatcher,
                itemAlbumModelsLocalFlow = itemAlbumModelsFlow,
                albumsCount = localAvailableAlbumsCount,
                error = error
            )
                .albums()
                .verifyLoadFromRemoteDataStore()
                .verifyInsertItemsToLocalDataStore()
                .verifyNetworkError(null, NetworkError(error))
        }


    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun `run failure Items available in local`() =
        coroutineTestExtension.testDispatcher.runBlockingTest {
            val localAvailableAlbumsCount = 5

            LoadAlbumsRepositoryImpRobo(
                this,
                coroutineTestExtension.testDispatcher,
                itemAlbumModelsLocalFlow = itemAlbumModelsFlow,
                albumsCount = localAvailableAlbumsCount,
                error = error
            )
                .albums()
                .verifyLoadFromRemoteDataStore()
                .verifyInsertItemsToLocalDataStore()
                .verifyNetworkError(null)
        }
}
