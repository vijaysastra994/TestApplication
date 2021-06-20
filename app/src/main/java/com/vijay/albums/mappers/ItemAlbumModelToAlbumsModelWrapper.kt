package com.vijay.albums.mappers

import com.vijay.albums.adapter.AlbumsListingItemTypes
import com.vijay.albums.adapter.AlbumsModelWrapper
import com.vijay.albums.adapter.listing.ItemAlbumUiModel
import com.vijay.albums.utils.Mapper
import javax.inject.Inject

class ItemAlbumModelToAlbumsModelWrapper @Inject constructor() :
    Mapper<ItemAlbumUiModel, AlbumsModelWrapper<*>> {

    override fun map(input: ItemAlbumUiModel): AlbumsModelWrapper<*> {
        return AlbumsModelWrapper(
            AlbumsListingItemTypes.SingleTitleItemType,
            input
        )
    }
}
