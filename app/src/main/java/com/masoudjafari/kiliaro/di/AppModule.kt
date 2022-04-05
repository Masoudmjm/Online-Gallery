package com.masoudjafari.kiliaro.di

import android.content.Context
import androidx.room.Room
import com.masoudjafari.kiliaro.data.source.DefaultImagesRepository
import com.masoudjafari.kiliaro.data.source.ImagesDataSource
import com.masoudjafari.kiliaro.data.source.ImagesRepository
import com.masoudjafari.kiliaro.data.source.local.ImageDatabase
import com.masoudjafari.kiliaro.data.source.local.ImagesLocalDataSource
import com.masoudjafari.kiliaro.data.source.remote.ImagesRemoteDataSource
import com.masoudjafari.kiliaro.data.source.remote.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://api1.kiliaro.com"

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): RetrofitService = retrofit.create(RetrofitService::class.java)

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteImagesDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalImagesDataSource

    @Singleton
    @RemoteImagesDataSource
    @Provides
    fun provideImagesRemoteDataSource(
        retrofitService: RetrofitService,
        ioDispatcher: CoroutineDispatcher
    ): ImagesDataSource {
        return ImagesRemoteDataSource(
            retrofitService,
            ioDispatcher
        )
    }

    @Singleton
    @LocalImagesDataSource
    @Provides
    fun provideImagesLocalDataSource(
        database: ImageDatabase,
        ioDispatcher: CoroutineDispatcher
    ): ImagesDataSource {
        return ImagesLocalDataSource(
            database.imageDao(),
            ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ImageDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ImageDatabase::class.java,
            "Images.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
object ImagesRepositoryModule {

    @Singleton
    @Provides
    fun provideImagesRepository(
        @AppModule.RemoteImagesDataSource remoteImagesDataSource: ImagesDataSource,
        @AppModule.LocalImagesDataSource localImagesDataSource: ImagesDataSource
    ): ImagesRepository {
        return DefaultImagesRepository(
            remoteImagesDataSource, localImagesDataSource
        )
    }
}