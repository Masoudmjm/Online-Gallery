package com.masoudjafari.kiliaro.images

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.masoudjafari.kiliaro.data.Image

/**
 * [BindingAdapter]s for the [Image]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Image>?) {
    items?.let {
        (listView.adapter as ImagesAdapter).submitList(items)
    }
}
