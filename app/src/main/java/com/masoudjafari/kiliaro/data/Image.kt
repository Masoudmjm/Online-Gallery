package com.masoudjafari.kiliaro.data

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide

@Entity
data class Image(
    @PrimaryKey val id: String,
    val user_id: String?,
    val media_type: String?,
    val filename: String?,
    val size: Int?,
    val created_at: String?,
    val taken_at: String?,
    val guessed_taken_at: String?,
    val md5sum: String?,
    val content_type: String?,
    val video: String?,
    val thumbnail_url: String?,
    val download_url: String?,
    val resx: Int?,
    val resy: Int?
)


