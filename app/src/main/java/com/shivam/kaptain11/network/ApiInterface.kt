package com.shivam.mvvmretrofit.network

import com.shivam.kaptain11.models.*
import com.shivam.kaptain11.util.Constants.Companion.FOLLOWER_AUTH_TOKEN
import com.shivam.kaptain11.util.Constants.Companion.GURU_AUTH_TOKEN
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("userService/getGuruByCode")
    @Headers("Authorization: $FOLLOWER_AUTH_TOKEN")
    suspend fun getGuruByCode(
        @Query("code") code: String,
    ): Response<GuruCodeResponse>

    @FormUrlEncoded
    @POST("userService/makeAsGuru")
    @Headers("Authorization: $FOLLOWER_AUTH_TOKEN")
    suspend fun makeSomeoneAsGuru(
        @Field("guruId") code: String,
    ): Response<MakeAsGuruResponse>

    @GET("walletService/getGuruDetails")
    @Headers("Authorization: $GURU_AUTH_TOKEN")
    suspend fun getGuruDetails(): Response<GetGuruDetailResponse>

    @GET("walletService/getGuruWinnings/{offset}")
    @Headers("Authorization: $GURU_AUTH_TOKEN")
    suspend fun getRecentWinnings(
        @Path("offset") offset : Int
    ): Response<GetGuruWinningsResponse>

    @GET("walletService/getGuruUserEarnings/{offset}")
    @Headers("Authorization: $GURU_AUTH_TOKEN")
    suspend fun getUserWiseWinnings(
        @Path("offset") offset : Int
    ): Response<GetUserWiseWinningResponse>
}