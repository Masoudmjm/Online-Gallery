package com.masoudjafari.kiliaro.data.source

import androidx.lifecycle.LiveData
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.data.Result

interface ImagesDataSource {

    fun observeTasks(): LiveData<Result<List<Image>>>

    suspend fun getImages() : Result<List<Image>>

    suspend fun refreshImages()

    suspend fun saveImage(image: Image)

    suspend fun deleteAllImages()

    suspend fun getId() : String
}