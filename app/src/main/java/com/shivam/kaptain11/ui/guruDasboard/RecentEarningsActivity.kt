package com.shivam.kaptain11.ui.guruDasboard

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivam.kaptain11.adapters.RecentEarningsAdapter
import com.shivam.kaptain11.base.BaseActivity
import com.shivam.kaptain11.databinding.ActivityRecentEarningsBinding
import com.shivam.kaptain11.util.Resource
import kotlinx.android.synthetic.main.activity_recent_earnings.*

class RecentEarningsActivity : BaseActivity() {
    private lateinit var binding: ActivityRecentEarningsBinding
    lateinit var viewModel: GuruProfileViewModel
    private lateinit var mAdapter: RecentEarningsAdapter

    companion object {
        const val TAG = "RecentEarningsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentEarningsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        setupUI()
        initializeObserver()
        viewModel.fetchRecentWinnings()
    }

    private fun initViewModel() {
        val viewModelProviderFactory =
            GuruProfileViewModelProviderFactory(application, GuruProfileRepository())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(GuruProfileViewModel::class.java)
    }

    private fun initializeObserver() {
        viewModel.recentWinningsResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { getGuruWinningResponse ->

                        mAdapter.updateData(getGuruWinningResponse.info.output)
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured $message")
                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
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
            rvRecentEarnings.apply {
                layoutManager = LinearLayoutManager(this@RecentEarningsActivity)
                mAdapter =
                    RecentEarningsAdapter(this@RecentEarningsActivity)
                adapter = mAdapter
            }
        }
    }
}