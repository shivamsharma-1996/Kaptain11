package com.shivam.kaptain11.ui.makeAsGuru

import com.shivam.kaptain11.network.RetroInstance

class MakeGuruRepository {
    suspend fun getGuruByCode(guruCode :String) =
        RetroInstance.apiInterface.getGuruByCode(guruCode)

    suspend fun makeSomeoneAsGuru(guruId :String) =
        RetroInstance.apiInterface.makeSomeoneAsGuru(guruId)
}