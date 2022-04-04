package com.masoudjafari.kiliaro.data.source.remote

import com.masoudjafari.kiliaro.data.Image
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
interface RetrofitService {

    @GET("/shared/djlCbGusTJamg_ca4axEVw/media")
    suspend fun getAll() : List<Image>
}