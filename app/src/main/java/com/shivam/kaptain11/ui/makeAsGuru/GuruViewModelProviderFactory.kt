package com.shivam.kaptain11.ui.makeAsGuru

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GuruViewModelProviderFactory(
    val app: Application,
    private val makeGuruRepository: MakeGuruRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MakeGuruViewModel(app, makeGuruRepository) as T
    }
}