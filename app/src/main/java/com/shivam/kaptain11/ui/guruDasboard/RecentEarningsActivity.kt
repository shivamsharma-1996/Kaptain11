package com.shivam.kaptain11.ui.guruDasboard

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivam.kaptain11.adapters.RecentEarningsAdapter
import com.shivam.kaptain11.base.BaseActivity
import com.shivam.kaptain11.databinding.ActivityRecentEarningsBinding
import com.shivam.kaptain11.util.Constants.Companion.QUERY_PAGE_SIZE
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
                    binding.swipeRefreshLayout.isRefreshing = false
                    response.data?.let { getGuruWinningResponse ->
                        mAdapter.updateData(getGuruWinningResponse.info.output)
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    binding.swipeRefreshLayout.isRefreshing = false
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured $message")
                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showLoading()
                    binding.swipeRefreshLayout.isRefreshing = false
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
                mAdapter = RecentEarningsAdapter(this@RecentEarningsActivity)
                adapter = mAdapter
                //addOnScrollListener(this@RecentEarningsActivity.scrollListener)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchRecentWinnings()
            }
        }
    }


    private var isLoading = false        //is the data is loading
    private var isLastPage = false       //to determine if we should stop paginating
    private var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtbeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtbeginning && isTotalMoreThanVisible
                        && isScrolling
            if (shouldPaginate) {
                viewModel.fetchRecentWinnings()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

        }
    }
}