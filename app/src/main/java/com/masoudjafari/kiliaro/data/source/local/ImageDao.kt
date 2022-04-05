package com.masoudjafari.kiliaro.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.masoudjafari.kiliaro.data.Image

@Dao
interface ImageDao {

    @Query("SELECT * FROM Image")
    fun observeImages(): LiveData<List<Image>>

    @Query("SELECT * FROM Image")
    fun getAll(): List<Image>

    @Query("SELECT * FROM Image WHERE id = :imageId")
    fun observeImageById(imageId: String): LiveData<Image>

    @Query("DELETE FROM Image")
    fun deleteImages()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)
}