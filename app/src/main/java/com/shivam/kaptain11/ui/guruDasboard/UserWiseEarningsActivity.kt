package com.shivam.kaptain11.ui.guruDasboard

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivam.kaptain11.adapters.UserWiseEarningsAdapter
import com.shivam.kaptain11.base.BaseActivity
import com.shivam.kaptain11.databinding.ActivityUserwiseEarningsBinding
import com.shivam.kaptain11.util.Resource
import com.shivam.kaptain11.util.showToast

class UserWiseEarningsActivity : BaseActivity()  {
    private lateinit var binding: ActivityUserwiseEarningsBinding
    lateinit var viewModel: GuruProfileViewModel
    private lateinit var mAdapter: UserWiseEarningsAdapter

    companion object {
        const val TAG = "UserWiseEarningsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserwiseEarningsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        setupUI()
        initializeObserver()
        viewModel.fetchUserWiseWinnings()
    }

    private fun initViewModel() {
        val viewModelProviderFactory =
            GuruProfileViewModelProviderFactory(application, GuruProfileRepository())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(GuruProfileViewModel::class.java)
    }

    private fun initializeObserver() {
        viewModel.userWiseWinningsResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { userWiseWinningResponse ->

                        mAdapter.updateData(userWiseWinningResponse.info)
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Log.d(RecentEarningsActivity.TAG, "An error occured $message")
                        showToast("An error occured: $message")
//                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }

        })

    }

    private fun setupUI() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            rvUserwiseEarnings.apply {
                layoutManager = LinearLayoutManager(this@UserWiseEarningsActivity)
                mAdapter =
                    UserWiseEarningsAdapter(this@UserWiseEarningsActivity)
                adapter = mAdapter
            }
        }
    }

}