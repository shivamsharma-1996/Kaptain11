package com.shivam.kaptain11.models

data class GetUserWiseWinningResponse(
    val description: String,
    val errorCode: Int,
    val info: List<UserWiseWinningInfo>,
    val success: Boolean
)