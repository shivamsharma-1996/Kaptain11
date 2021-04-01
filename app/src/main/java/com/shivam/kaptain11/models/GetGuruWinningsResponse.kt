package com.shivam.kaptain11.models

data class GetGuruWinningsResponse(
    val description: String,
    val errorCode: Int,
    val info: GuruWinningsInfo,
    val success: Boolean
)