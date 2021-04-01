package com.shivam.kaptain11.models

data class GetGuruDetailResponse(
    val description: String,
    val errorCode: Int,
    val info: GuruDetailInfo,
    val success: Boolean
)