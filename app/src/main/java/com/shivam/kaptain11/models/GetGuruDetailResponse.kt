package com.shivam.kaptain11.models

data class GetGuruDetailResponse(
    val description: String,
    val errorCode: Int,
    val info: InfoXX,
    val success: Boolean
)