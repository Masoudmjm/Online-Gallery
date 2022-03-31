package com.masoudjafari.kiliaro.data.source.remote

import androidx.lifecycle.LiveData
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.source.ImagesDataSource
import com.masoudjafari.kiliaro.data.Image
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImagesRemoteDataSource internal constructor(
    private val retrofitService: RetrofitService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ImagesDataSource {

    override fun observeTasks(): LiveData<Result<List<Image>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getImages(): Result<List<Image>> = withContext(ioDispatcher){
        return@withContext try {
            Result.Success(retrofitService.getAll())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun refreshImages() {
    }

    override suspend fun saveImage(image: Image) {
    }

    override suspend fun deleteAllImages() {
    }

    override suspend fun getId(): String {
        TODO("Not yet implemented")
    }
}