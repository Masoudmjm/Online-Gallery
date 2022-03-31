package com.masoudjafari.kiliaro.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.masoudjafari.kiliaro.data.Image

@Database(entities = [Image::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}