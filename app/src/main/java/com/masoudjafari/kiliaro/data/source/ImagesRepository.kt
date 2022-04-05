package com.masoudjafari.kiliaro.data.source

import androidx.lifecycle.LiveData
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.data.Result

interface ImagesRepository {

    fun observeImages(): LiveData<Result<List<Image>>>

    suspend fun getImages(forceUpdate: Boolean = false) : Result<List<Image>>

    suspend fun refreshImages()

    fun observeImage(imageId: String): LiveData<Result<Image>>
}