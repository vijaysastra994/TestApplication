package com.vijay.albums

import com.vijay.albums.adapter.AlbumsListingItemTypes
import com.vijay.albums.adapter.AlbumsModelWrapper
import com.vijay.albums.adapter.listing.ItemAlbumUiModel
import com.vijay.albums.usecases.repository.NetworkError
import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import com.vijay.albums.utils.CoroutineTestExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension


class AlbumsFragmentViewModelTest {

    @ExperimentalCoroutinesApi
    @RegisterExtension
    @JvmField
    val coroutineTestExtension = CoroutineTestExtension()

    private val itemAlbumModel = ItemAlbumModel(1, "title")
    private val itemAlbumModels = listOf(itemAlbumModel)
    private val itemAlbumUiModel = ItemAlbumUiModel(1, "title")
    private val albumsModelWrappers =
        listOf(AlbumsModelWrapper(AlbumsListingItemTypes.SingleTitleItemType, itemAlbumUiModel))

    private val loading = ViewState(showLoading = true, items = null)
    private val success = ViewState(showLoading = false, items = albumsModelWrappers)
    private val error = IllegalStateException("something went wrong :O")

    @ExperimentalCoroutinesApi
    @Test
    fun `load Albums success`() = coroutineTestExtension.testDispatcher.runBlockingTest {
        AlbumsFragmentViewModelRobo(
            this,
            coroutineTestExtension.testDispatcher,
            null,
            MutableStateFlow(itemAlbumModels)
        )
            .viewCreated()
            .verifyViewStates(loading, success)
            .verifyError(null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `load Albums failure`() = coroutineTestExtension.testDispatcher.runBlockingTest {
        AlbumsFragmentViewModelRobo(
            this,
            coroutineTestExtension.testDispatcher,
            error = error
        )
            .viewCreated()
            .verifyViewStates(loading)
            .verifyError(null, NetworkError())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `retry load Albums success`() = coroutineTestExtension.testDispatcher.runBlockingTest {
        AlbumsFragmentViewModelRobo(
            this,
            coroutineTestExtension.testDispatcher,
            null,
            MutableStateFlow(itemAlbumModels)
        )
            .retry()
            .verifyViewStates(loading, success)
            .verifyError(null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `retry load Albums failure`() = coroutineTestExtension.testDispatcher.runBlockingTest {
        AlbumsFragmentViewModelRobo(
            this,
            coroutineTestExtension.testDispatcher,
            error
        )
            .retry()
            .verifyViewStates(loading)
            .verifyError(null, NetworkError())
    }
}