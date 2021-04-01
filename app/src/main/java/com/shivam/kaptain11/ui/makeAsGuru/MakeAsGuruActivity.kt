package com.shivam.kaptain11.ui.makeAsGuru

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shivam.kaptain11.R
import com.shivam.kaptain11.base.BaseActivity
import com.shivam.kaptain11.databinding.ActivityMakeAsGuruBinding
import com.shivam.kaptain11.models.Info
import com.shivam.kaptain11.util.Resource
import com.shivam.kaptain11.util.showToast
import kotlinx.android.synthetic.main.activity_make_as_guru.*
import kotlinx.android.synthetic.main.sheet_guru_added.view.*

class MakeAsGuruActivity : BaseActivity(), GuruRetrievedSheetFragment.OnGuruAddedListener {
    private lateinit var binding: ActivityMakeAsGuruBinding
    private lateinit var viewModel: MakeGuruViewModel

    companion object {
        const val TAG = "MakeAsGuruActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeAsGuruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        initializeObserver()
    }

    private fun setup() {
        val viewModelProviderFactory = GuruViewModelProviderFactory(application, MakeGuruRepository())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MakeGuruViewModel::class.java)

        binding.apply {
            findButton.setOnClickListener {
                val guruCode = etGuruCode.text.toString().toUpperCase()
                viewModel.getGuruByCode(guruCode)
            }
        }
    }

    private fun initializeObserver() {
        viewModel.guruDetailByCode.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { guruCodeResponse ->
                        if (guruCodeResponse.success) {
                            displayGuruInfo(guruCodeResponse.info)
                        } else {
                            showToast("${guruCodeResponse.description}")
                        }
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Log.d(TAG, " $message")
                        showToast("$message")
                    }
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        })

        viewModel.makeAsGuruLiveData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    //hideLoading()
                    response.data?.let { makeAsGuruResponse ->
                        if (makeAsGuruResponse.success) {
                            displayGuruAddedSheet()
                        } else {
                            showToast("An error occured: ${makeAsGuruResponse.description}")
                        }
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured $message")
                        showToast("An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        })
    }

    private fun displayGuruInfo(guruInfo: Info) {
        val guruRetrievedSheetFragment = GuruRetrievedSheetFragment.newInstance(
            this,
            guruInfo.guru_id,
            guruInfo.code,
            guruInfo.username,
            guruInfo.followers_count.toString()
        )
        guruRetrievedSheetFragment.show(
            supportFragmentManager,
            GuruRetrievedSheetFragment.javaClass.name
        )
    }

    override fun onGuruAdded() {
        displayGuruAddedSheet()
    }

    private fun displayGuruAddedSheet() {
        val bottomSheetView = layoutInflater.inflate(R.layout.sheet_guru_added, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomSheetView)
        bottomSheetView.btnOk.setOnClickListener {
            dialog.dismiss()
        }
        etGuruCode.setText("")
        dialog.show()
    }
}

