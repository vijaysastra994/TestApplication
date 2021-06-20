package com.vijay.albums

import com.vijay.albums.mappers.ItemAlbumModelToAlbumsModelWrapper
import com.vijay.albums.mappers.ItemAlbumModelToItemAlbumUiModel
import com.vijay.albums.usecases.LoadAlbumsUseCase
import com.vijay.albums.usecases.repository.NetworkError
import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.jupiter.api.Assertions.assertEquals

@ExperimentalCoroutinesApi
class AlbumsFragmentViewModelRobo @ExperimentalCoroutinesApi constructor(
    testCoroutineScope: TestCoroutineScope,
    testCoroutineDispatcher: TestCoroutineDispatcher,
    val error: Exception? = null,
    vararg albumsResponseFlow: MutableStateFlow<List<ItemAlbumModel>>
) {

    private val viewModel: AlbumsFragmentViewModel
    private val loadAlbumsUseCase: LoadAlbumsUseCase = mock()
    private val errorStateFlow = MutableStateFlow<NetworkError?>(null)

    /**
     * didn't mock mappers since mock will do the same job as implementation itself
     */
    private val itemAlbumModelToAlbumsModelWrapper =
        ItemAlbumModelToAlbumsModelWrapper()
    private val itemAlbumModelToItemAlbumUiModel =
        ItemAlbumModelToItemAlbumUiModel()
    private val viewStates = mutableListOf<ViewState>()
    private val errorStates = mutableListOf<NetworkError?>()
    private val viewStateJob: Job
    private val errorStateJob: Job

    init {
        mockLoadAlbumsUseCase(albumsResponseFlow)

        viewModel = AlbumsFragmentViewModel(
            testCoroutineDispatcher,
            loadAlbumsUseCase,
            itemAlbumModelToAlbumsModelWrapper,
            itemAlbumModelToItemAlbumUiModel
        )

        viewStateJob = testCoroutineScope.launch {
            viewModel.viewStateFlow.collect {
                viewStates.add(it)
            }
        }

        errorStateJob = testCoroutineScope.launch {
            viewModel.errorStateFlow.collect {
                errorStates.add(it)
            }
        }
    }

    private fun mockLoadAlbumsUseCase(albumsResponseFlow: Array<out MutableStateFlow<List<ItemAlbumModel>>>) {
        whenever(loadAlbumsUseCase.netWorkFailureStateFlow).thenReturn(errorStateFlow)
        loadAlbumsUseCase.stub {
            val stubbing = onBlocking { run(any()) }
            when {
                albumsResponseFlow.isEmpty() -> {
                    stubbing.thenThrow(error)
                }
                albumsResponseFlow.size == 1 -> {
                    stubbing.thenReturn(albumsResponseFlow[0])
                }
                albumsResponseFlow.size > 1 -> {
                    stubbing.thenReturn(
                        albumsResponseFlow[0],
                        *albumsResponseFlow.sliceArray(1 until albumsResponseFlow.size)
                    )
                }
            }
        }
    }

    fun viewCreated(): AlbumsFragmentViewModelRobo {
        viewModel.viewCreated()

        return this
    }

    fun verifyViewStates(vararg viewState: ViewState): AlbumsFragmentViewModelRobo {
        assertEquals(viewState.toList(), viewStates)
        viewStateJob.cancel()

        return this
    }

    fun retry(): AlbumsFragmentViewModelRobo {
        viewModel.retry()

        return this
    }

    fun verifyError(vararg networkError: NetworkError?): AlbumsFragmentViewModelRobo {
        assertEquals(networkError.toList(), errorStates)
        errorStateJob.cancel()

        return this
    }
}
