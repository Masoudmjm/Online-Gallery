package com.masoudjafari.kiliaro.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.databinding.ItemImageBinding

class ImagesAdapter(private val viewModel: ImagesViewModel) : ListAdapter<Image, ViewHolder>(
    ImagesDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)

        val transitionName = item.id
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
