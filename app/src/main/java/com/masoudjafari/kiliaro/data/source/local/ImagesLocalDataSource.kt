package com.masoudjafari.kiliaro.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.data.source.ImagesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImagesLocalDataSource internal constructor(
    private val imageDao: ImageDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ImagesDataSource {

    override fun observeImages(): LiveData<Result<List<Image>>> {
        return imageDao.observeImages().map {
            Result.Success(it)
        }
    }

    override suspend fun getImages(): Result<List<Image>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(imageDao.getAll())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun refreshImages() {
        TODO("Not yet implemented")
    }

    override fun observeImage(imageId: String): LiveData<Result<Image>> {
        return imageDao.observeImageById(imageId).map {
            Result.Success(it)
        }
    }

    override suspend fun saveImage(image: Image) {
        imageDao.insertImage(image)
    }

    override suspend fun deleteAllImages() = withContext(ioDispatcher) {
        imageDao.deleteImages()
    }

    override suspend fun getId(): String {
        TODO("Not yet implemented")
    }


}