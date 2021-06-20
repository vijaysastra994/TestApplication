package com.vijay.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vijay.albums.adapter.AlbumsModelWrapper
import com.vijay.albums.mappers.ItemAlbumModelToAlbumsModelWrapper
import com.vijay.albums.mappers.ItemAlbumModelToItemAlbumUiModel
import com.vijay.albums.usecases.LoadAlbumsUseCase
import com.vijay.albums.usecases.repository.NetworkError
import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumsFragmentViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val loadAlbumsUseCase: LoadAlbumsUseCase,
    private val itemAlbumModelToAlbumsModelWrapper: ItemAlbumModelToAlbumsModelWrapper,
    private val itemAlbumModelToItemAlbumUiModel: ItemAlbumModelToItemAlbumUiModel
) : ViewModel() {

    private val _viewStateFlow = MutableSharedFlow<Unit>()

    @ExperimentalCoroutinesApi
    val viewStateFlow: SharedFlow<ViewState> = _viewStateFlow
        .flatMapLatest {
            _errorStateFlow.emit(null)

            loadAlbumsUseCase.run(viewModelScope).map { items ->
                //showLoading = items.isEmpty() with assumption that we always have albums available,
                // so items.isEmpty() means when we don't have items in local db
                ViewState(items = mapItems(items), showLoading = items.isEmpty())
            }
        }.catch {
            //this shouldn't happen
            _errorStateFlow.emit(NetworkError())
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ViewState(showLoading = true))

    private val _errorStateFlow: MutableStateFlow<NetworkError?> =
        loadAlbumsUseCase.netWorkFailureStateFlow
    val errorStateFlow: StateFlow<NetworkError?> = _errorStateFlow

    private fun mapItems(items: List<ItemAlbumModel>): List<AlbumsModelWrapper<*>> {
        return items.map { itemAlbumModel ->
            itemAlbumModelToAlbumsModelWrapper.map(
                itemAlbumModelToItemAlbumUiModel.map(itemAlbumModel)
            )
        }
    }

    fun viewCreated() {
        viewModelScope.launch {
            _viewStateFlow.emit(Unit)
        }
    }

    fun retry() {
        viewModelScope.launch {
            _viewStateFlow.emit(Unit)
        }
    }
}

data class ViewState(
    val showLoading: Boolean = false,
    val items: List<AlbumsModelWrapper<*>>? = null
)
