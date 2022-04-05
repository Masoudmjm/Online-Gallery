package com.masoudjafari.kiliaro.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.source.ImagesDataSource
import com.masoudjafari.kiliaro.data.Image
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Error

class ImagesRemoteDataSource internal constructor(
    private val retrofitService: RetrofitService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ImagesDataSource {

    private val observableImages = MutableLiveData<Result<List<Image>>>()

    override suspend fun getImages(): Result<List<Image>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(retrofitService.getAll())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun refreshImages() {
        observableImages.value = getImages()!!
    }

    override fun observeImages(): LiveData<Result<List<Image>>> {
        return observableImages
    }

    override fun observeImage(imageId: String): LiveData<Result<Image>> {
        return observableImages.map { images ->
            when (images) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(images.exception)
                is Result.Success -> {
                    val image =
                        images.data.firstOrNull() { it.id == imageId } ?: return@map Result.Error(
                            Exception("Not found")
                        )
                    Result.Success(image)
                }
            }
        }
    }

    override suspend fun saveImage(image: Image) {}

    override suspend fun deleteAllImages() {}
}