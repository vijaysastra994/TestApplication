package com.vijay.albums.adapter

import com.mikepenz.fastadapter.items.ModelAbstractItem
import com.vijay.albums.adapter.listing.SingleTitleItem

class AlbumsItemFactory : (AlbumsModelWrapper<*>) -> ModelAbstractItem<*, *> {

    override fun invoke(itemWrapper: AlbumsModelWrapper<*>): ModelAbstractItem<*, *> {
        return when (itemWrapper.type) {
            AlbumsListingItemTypes.SingleTitleItemType -> {
                SingleTitleItem(itemWrapper)
            }
        }
    }
}
