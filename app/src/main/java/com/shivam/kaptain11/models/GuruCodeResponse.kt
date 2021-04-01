package com.shivam.kaptain11.models

data class GuruCodeResponse(
    val description: String,
    val errorCode: Int,
    val info: Info,
    val success: Boolean
)