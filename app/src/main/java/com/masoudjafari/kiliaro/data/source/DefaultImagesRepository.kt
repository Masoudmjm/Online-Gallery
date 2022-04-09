package com.masoudjafari.kiliaro.data.source

import androidx.lifecycle.LiveData
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.Result.Success
import com.masoudjafari.kiliaro.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultImagesRepository(
    private val imagesRemoteDataSource: ImagesDataSource,
    private val imagesLocalDataSource: ImagesDataSource,
) : ImagesRepository {

    override suspend fun getImages(forceUpdate: Boolean): Result<List<Image>> {
        wrapEspressoIdlingResource {
            if (forceUpdate) {
                try {
                    updateImagesFromRemoteDataSource()
                } catch (ex: Exception) {
                    return Result.Error(ex)
                }
            }
            return imagesLocalDataSource.getImages()
        }
    }

    override fun observeImages(): LiveData<Result<List<Image>>> {
        return imagesLocalDataSource.observeImages()
    }

    private suspend fun updateImagesFromRemoteDataSource() {
        val remoteImages = imagesRemoteDataSource.getImages()

        if (remoteImages is Success) {
            imagesLocalDataSource.deleteAllImages()
            remoteImages.data.forEach { image ->
                imagesLocalDataSource.saveImage(image)
            }
        } else if (remoteImages is Result.Error) {
            throw remoteImages.exception
        }
    }

    override fun observeImage(imageId: String): LiveData<Result<Image>> {
        return imagesLocalDataSource.observeImage(imageId)
    }
}