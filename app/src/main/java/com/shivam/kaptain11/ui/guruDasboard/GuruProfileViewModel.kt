package com.shivam.kaptain11.ui.guruDasboard

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.shivam.kaptain11.MyApplication
import com.shivam.kaptain11.models.GetGuruDetailResponse
import com.shivam.kaptain11.models.GetGuruWinningsResponse
import com.shivam.kaptain11.models.GetUserWiseWinningResponse
import com.shivam.kaptain11.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class GuruProfileViewModel(
    app: Application,
    private val guruProfileRepository: GuruProfileRepository
) : AndroidViewModel(app) {

    init {
//        fetchGuruDetails()
    }

    val guruDetailResponse: MutableLiveData<Resource<GetGuruDetailResponse>> = MutableLiveData()
    val recentWinningsResponse: MutableLiveData<Resource<GetGuruWinningsResponse>> = MutableLiveData()
    val userWiseWinningsResponse: MutableLiveData<Resource<GetUserWiseWinningResponse>> = MutableLiveData()

    var recentWinningPageNumber = 1
    var userWiseWinningPageNumber = 1


    fun fetchGuruDetails() = viewModelScope.launch(Dispatchers.IO) {
        guruDetailResponse.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = guruProfileRepository.getGuruDetails()
                guruDetailResponse.postValue(handleGetGuruDetailsResponse(response))
            } else {
                guruDetailResponse.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Throwable) {
            when (ex) {
                is IOException -> guruDetailResponse.postValue(Resource.Error("Network Failure"))
                else -> guruDetailResponse.postValue(Resource.Error("Parsing Error"))
            }
        }
    }

    fun fetchRecentWinnings() = viewModelScope.launch(Dispatchers.IO) {
        recentWinningsResponse.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = guruProfileRepository.getRecentWinnings(recentWinningPageNumber)
                recentWinningsResponse.postValue(handleRecentWinningsResponse(response))
            } else {
                recentWinningsResponse.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Throwable) {
            when (ex) {
                is IOException -> recentWinningsResponse.postValue(Resource.Error("Network Failure"))
                else -> recentWinningsResponse.postValue(Resource.Error("Parsing Error"))
            }
        }
    }

    fun fetchUserWiseWinnings() = viewModelScope.launch(Dispatchers.IO) {
        userWiseWinningsResponse.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = guruProfileRepository.getUserWiseWinnings(recentWinningPageNumber)
                userWiseWinningsResponse.postValue(handleUserWiseWinningsResponse(response))
            } else {
                userWiseWinningsResponse.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Throwable) {
            when (ex) {
                is IOException -> userWiseWinningsResponse.postValue(Resource.Error("Network Failure"))
                else -> userWiseWinningsResponse.postValue(Resource.Error("Parsing Error"))
            }
        }
    }

    private fun handleGetGuruDetailsResponse(response: Response<GetGuruDetailResponse>): Resource<GetGuruDetailResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRecentWinningsResponse(response: Response<GetGuruWinningsResponse>): Resource<GetGuruWinningsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleUserWiseWinningsResponse(response: Response<GetUserWiseWinningResponse>): Resource<GetUserWiseWinningResponse> {
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
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}