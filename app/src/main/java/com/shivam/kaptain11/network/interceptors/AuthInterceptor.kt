package com.shivam.kaptain11.network.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
//        val accessToken = SharedPref.getStringPref(context, SharedPref.KEY_AUTH_TOKEN)
//        requestBuilder.addHeader("User-Agent", "Android")
//        if (accessToken.isNotBlank())
//            requestBuilder.addHeader("Authorization", accessToken)
        return chain.proceed(requestBuilder.build())
    }
}
