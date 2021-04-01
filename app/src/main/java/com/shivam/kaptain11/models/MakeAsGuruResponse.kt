package com.shivam.kaptain11.models

data class MakeAsGuruResponse(
    val description: String,
    val errorCode: Int,
    val info: InfoX,
    val success: Boolean
)