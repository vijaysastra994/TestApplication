package com.vijay.albums.adapter.listing

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.ModelAbstractItem
import com.vijay.albums.R
import com.vijay.albums.adapter.AlbumsListingItemTypes
import com.vijay.albums.adapter.AlbumsModelWrapper
import com.vijay.albums.databinding.ItemAlbumBinding

class SingleTitleItem(wrapper: AlbumsModelWrapper<*>) :
    ModelAbstractItem<AlbumsModelWrapper<*>, RecyclerView.ViewHolder>(wrapper) {
    override val layoutRes: Int = R.layout.item_album
    override val type: Int = AlbumsListingItemTypes.SingleTitleItemType.value

    private val itemAlbumsModel = wrapper.model as ItemAlbumUiModel

    override fun getViewHolder(v: View): RecyclerView.ViewHolder {
        return ViewHolder(v)
    }

    override fun bindView(holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)
        (holder as ViewHolder).binding.titleTextView.text = itemAlbumsModel.title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemAlbumBinding.bind(itemView)
    }
}
