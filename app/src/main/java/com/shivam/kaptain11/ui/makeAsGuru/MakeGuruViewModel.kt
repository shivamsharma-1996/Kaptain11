package com.shivam.kaptain11.ui.makeAsGuru

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shivam.kaptain11.MyApplication
import com.shivam.kaptain11.models.GuruCodeResponse
import com.shivam.kaptain11.models.MakeAsGuruResponse
import com.shivam.kaptain11.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MakeGuruViewModel(
    app: Application,
    private val makeGuruRepository: MakeGuruRepository
) : AndroidViewModel(app) {

    val guruDetailByCode: MutableLiveData<Resource<GuruCodeResponse>> = MutableLiveData()
    val makeAsGuruLiveData: MutableLiveData<Resource<MakeAsGuruResponse>> = MutableLiveData()

    fun getGuruByCode(guruCode: String) = viewModelScope.launch(Dispatchers.IO) {
        safeGuruByCodeCall(guruCode)
    }

    fun makeSomeoneAsGuru(guruId: String) = viewModelScope.launch(Dispatchers.IO) {
        makeAsGuruLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = makeGuruRepository.makeSomeoneAsGuru(guruId)
                makeAsGuruLiveData.postValue(handleMakeAsGuruResponse(response))
            } else {
                makeAsGuruLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Throwable) {
            when (ex) {
                is IOException -> guruDetailByCode.postValue(Resource.Error("Network Failure"))
                else -> guruDetailByCode.postValue(Resource.Error("Parsing Error"))
            }
        }
    }

    private suspend fun safeGuruByCodeCall(code: String) {
        guruDetailByCode.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = makeGuruRepository.getGuruByCode(code)
                guruDetailByCode.postValue(handleGuruByCodeResponse(response))
            } else {
                guruDetailByCode.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Throwable) {
            when (ex) {
                is IOException -> guruDetailByCode.postValue(Resource.Error("Network Failure"))
                else -> guruDetailByCode.postValue(Resource.Error("Parsing Error"))
            }
        }
    }

    private fun handleGuruByCodeResponse(response: Response<GuruCodeResponse>): Resource<GuruCodeResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleMakeAsGuruResponse(response: Response<MakeAsGuruResponse>): Resource<MakeAsGuruResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}