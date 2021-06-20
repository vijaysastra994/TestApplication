package com.vijay.albums.mappers

import com.vijay.albums.adapter.listing.ItemAlbumUiModel
import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import com.vijay.albums.utils.Mapper
import javax.inject.Inject

class ItemAlbumModelToItemAlbumUiModel @Inject constructor() :
    Mapper<ItemAlbumModel, ItemAlbumUiModel> {

    override fun map(input: ItemAlbumModel): ItemAlbumUiModel {
        return ItemAlbumUiModel(input.id, input.title)
    }
}
