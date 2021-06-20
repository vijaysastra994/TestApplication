package com.vijay.albums.adapter

import com.vijay.albums.adapter.AlbumsModelWrapper.Companion.ALBUMS_LISTING_ITEM

sealed class AlbumsListingItemTypes {

    object SingleTitleItemType : AlbumsListingItemTypes(){
        const val value: Int = ALBUMS_LISTING_ITEM
    }
}

data class AlbumsModelWrapper<MODEL>(
    val type: AlbumsListingItemTypes,
    val model: MODEL
) {
    companion object {
        const val ALBUMS_LISTING_ITEM = 1001
    }
}
