package com.vijay.albums.mappers

import com.vijay.albums.adapter.AlbumsListingItemTypes
import com.vijay.albums.adapter.AlbumsModelWrapper
import com.vijay.albums.adapter.listing.ItemAlbumUiModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ItemAlbumModelToAlbumsModelWrapperTest {

    private val itemAlbumModel = ItemAlbumUiModel(1, "title")
    private val albumsModelWrapper =
        AlbumsModelWrapper(AlbumsListingItemTypes.SingleTitleItemType, itemAlbumModel)

    @Test
    fun map() {
        val albumsModelWrapperResult = ItemAlbumModelToAlbumsModelWrapper()
            .map(itemAlbumModel)

        assertEquals(albumsModelWrapper, albumsModelWrapperResult)
    }
}