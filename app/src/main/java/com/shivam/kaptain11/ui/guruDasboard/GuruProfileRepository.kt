package com.shivam.kaptain11.ui.guruDasboard

import com.shivam.kaptain11.network.RetroInstance

class GuruProfileRepository {

    suspend fun getGuruDetails() =
        RetroInstance.apiInterface.getGuruDetails()

    suspend fun getRecentWinnings(offset: Int) =
        RetroInstance.apiInterface.getRecentWinnings(offset)

    suspend fun getUserWiseWinnings(offset: Int) =
        RetroInstance.apiInterface.getUserWiseWinnings(offset)
}