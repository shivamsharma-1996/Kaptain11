package com.shivam.kaptain11.network

import com.shivam.kaptain11.util.Constants.Companion.BASE_URL
import com.shivam.mvvmretrofit.network.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {
    companion object{
//        const val BASE_URL = "https://k11api.fansedge.in/"

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val apiInterface by lazy {
            retrofit.create(ApiInterface::class.java)
        }
    }
}