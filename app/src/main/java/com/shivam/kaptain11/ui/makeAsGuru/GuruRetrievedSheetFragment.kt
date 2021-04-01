package com.shivam.kaptain11.ui.makeAsGuru

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shivam.kaptain11.databinding.FragmentGuruRetrievedSheetBinding
import com.shivam.kaptain11.util.Resource
import com.shivam.kaptain11.util.showToast

class GuruRetrievedSheetFragment : BottomSheetDialogFragment() {
    private var _binding :  FragmentGuruRetrievedSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelMake: MakeGuruViewModel

    private lateinit var onGuruAddedListener : OnGuruAddedListener
    private var _guruId: String? = null
    private var _guruName: String? = null
    private var _guruCode: String? = null
    private var _guruFollowersCount: String? = null


    interface OnGuruAddedListener{
        fun onGuruAdded()
    }

    companion object {
        const val EXTRAS_GURU_ID = "extras_guru_id"
        const val EXTRAS_GURU_NAME = "extras_guru_name"
        const val EXTRAS_GURU_CODE = "extras_guru_code"
        const val EXTRAS_GURU_FOLLOWERS_COUNT = "extras_guru_followers_count"

        @JvmStatic
        fun newInstance(onGuruAddedListener : OnGuruAddedListener, guruId: String, code: String, username: String, followersCount: String) =
            GuruRetrievedSheetFragment().apply {
                this.onGuruAddedListener = onGuruAddedListener
                arguments = Bundle().apply {
                    putString(EXTRAS_GURU_ID, guruId)
                    putString(EXTRAS_GURU_NAME, username)
                    putString(EXTRAS_GURU_CODE, code)
                    putString(EXTRAS_GURU_FOLLOWERS_COUNT, followersCount)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _guruId = it.getString(EXTRAS_GURU_ID)
            _guruName = it.getString(EXTRAS_GURU_NAME)
            _guruCode = it.getString(EXTRAS_GURU_CODE)
            _guruFollowersCount = it.getString(EXTRAS_GURU_FOLLOWERS_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuruRetrievedSheetBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeObserver()
    }

    private fun setupUI() {
        val viewModelProviderFactory = GuruViewModelProviderFactory(activity!!.application, MakeGuruRepository())
        viewModelMake = ViewModelProvider(this, viewModelProviderFactory).get(MakeGuruViewModel::class.java)

        binding.apply {
            guruCode.text = _guruCode
            tvGuruProfileName.text = _guruName
            tvGuruFollowersCount.text = _guruFollowersCount
            btnMakeMyGuru.setOnClickListener {
                //send the guruId to viewModel here
                _guruId?.let {
                    viewModelMake.makeSomeoneAsGuru(_guruId!!)
                }
            }
        }
    }

    private fun initializeObserver() {
        viewModelMake.makeAsGuruLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { makeAsGuruResponse ->
                        if (makeAsGuruResponse.success) {
                            //displayGuruInfo(makeAsGuruResponse.info)
                            onGuruAddedListener.onGuruAdded()
                            this.dismiss()
                        } else {
                            Toast.makeText(
                                activity,
                                "An error occured: ${makeAsGuruResponse.description}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Log.d(MakeAsGuruActivity.TAG, "An error occured $message")
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        })
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}