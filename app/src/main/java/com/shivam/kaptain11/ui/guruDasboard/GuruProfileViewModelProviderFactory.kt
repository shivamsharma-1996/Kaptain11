package com.shivam.kaptain11.ui.guruDasboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GuruProfileViewModelProviderFactory(
    val app: Application,
    private val guruProfileRepository: GuruProfileRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GuruProfileViewModel(app, guruProfileRepository) as T
    }
}