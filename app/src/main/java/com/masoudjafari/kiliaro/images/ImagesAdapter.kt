package com.masoudjafari.kiliaro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.databinding.ItemImageBinding
import com.masoudjafari.kiliaro.images.ImagesViewModel

class ImagesAdapter(private val viewModel: ImagesViewModel) :
    ListAdapter<Image, ViewHolder>(ImagesDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
        Glide.with(holder.itemView.context)
            .load(item.thumbnail_url)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(holder.binding.imageIv)

        val transitionName = "ProductDetailTransition$position"
        holder.binding.imageIv.transitionName = transitionName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
}

class ViewHolder private constructor(val binding: ItemImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: ImagesViewModel, item: Image) {

        binding.viewmodel = viewModel
        binding.image = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemImageBinding.inflate(layoutInflater, parent, false)

            return ViewHolder(binding)
        }
    }
}

class ImagesDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}
