package com.shivam.kaptain11.ui.guruDasboard

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivam.kaptain11.adapters.UserWiseEarningsAdapter
import com.shivam.kaptain11.base.BaseActivity
import com.shivam.kaptain11.databinding.ActivityUserwiseEarningsBinding
import com.shivam.kaptain11.util.Constants
import com.shivam.kaptain11.util.Resource
import com.shivam.kaptain11.util.showToast

class UserWiseEarningsActivity : BaseActivity() {
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
                    binding.swipeRefreshLayout.isRefreshing = false
                    response.data?.let { userWiseWinningResponse ->

                        mAdapter.updateData(userWiseWinningResponse.info)
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    binding.swipeRefreshLayout.isRefreshing = false
                    response.message?.let { message ->
                        Log.d(RecentEarningsActivity.TAG, "An error occured $message")
                        showToast("An error occured: $message")
//                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = false
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
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        LinearLayoutManager.HORIZONTAL
                    )
                )
                mAdapter = UserWiseEarningsAdapter(this@UserWiseEarningsActivity)
                adapter = mAdapter
                //addOnScrollListener(this@UserWiseEarningsActivity.scrollListener)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchUserWiseWinnings()
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
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