package com.shivam.kaptain11.ui.guruDasboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shivam.kaptain11.R
import com.shivam.kaptain11.base.BaseActivity
import com.shivam.kaptain11.databinding.ActivityGuruDashboardBinding
import com.shivam.kaptain11.models.GuruDetailInfo
import com.shivam.kaptain11.util.*
import kotlinx.android.synthetic.main.activity_make_as_guru.*
import kotlinx.android.synthetic.main.item_userwise_earnings.*
import kotlinx.android.synthetic.main.layout_guru_earnings_stat.*
import kotlinx.android.synthetic.main.sheet_guru_added.view.*
import kotlinx.android.synthetic.main.sheet_update_guru_name.view.*
import java.io.File

class GuruDashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityGuruDashboardBinding
    private lateinit var viewModel: GuruProfileViewModel

    companion object {
        const val TAG = "GuruDashboardActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuruDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        initializeObserver()
        viewModel.fetchGuruDetails()
    }

    private fun initializeObserver() {
        viewModel.guruDetailResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { getGuruDetailResponse ->
                        if (getGuruDetailResponse.success) {
                            val guruDetailResponse = getGuruDetailResponse.info
                            updateUI(guruDetailResponse)
                        } else {
                            showToast("" + getGuruDetailResponse.description)
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

    private fun updateUI(guruDetailResponse: GuruDetailInfo) {
        binding.apply {
            tvGuruName.text = guruDetailResponse.username
            tvTotalEarning.text = "₹" + guruDetailResponse.dayEarnings
            tvDayEarning.text = "₹" + guruDetailResponse.dayEarnings
            tvGuruCode.text = guruDetailResponse.code
            val profilePicUrl = guruDetailResponse.display_picture
            profilePicUrl?.let {
                Glide.with(this@GuruDashboardActivity).load(guruDetailResponse.display_picture)
                    .placeholder(R.drawable.ic_profile_default).into(ivGuruImage)
            }
        }
    }

    private fun setup() {
        val viewModelProviderFactory =
            GuruProfileViewModelProviderFactory(application, GuruProfileRepository())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(GuruProfileViewModel::class.java)

        binding.apply {
            ivGuruImage.setOnClickListener {
                ImagePicker.with(this@GuruDashboardActivity)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    )    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
            ivBack.setOnClickListener {
                finish()
            }
            tvCopyCode.setOnClickListener {
                tvGuruCode?.let {
                    copyToClipboard(tvGuruCode.text.toString())
                }
            }
            ivEditGuruProfile.setOnClickListener {
                launchUpdateGuruSheet()
            }

            btnShareApp.setOnClickListener {
                shareIntent()
            }

            cvRecentEarnings.setOnClickListener {
                startActivity(
                    Intent(
                        this@GuruDashboardActivity,
                        RecentEarningsActivity::class.java
                    )
                )
            }
            cvUserWiseEarnings.setOnClickListener {
                startActivity(
                    Intent(
                        this@GuruDashboardActivity,
                        UserWiseEarningsActivity::class.java
                    )
                )
            }
        }
    }

    private fun launchUpdateGuruSheet() {
        val bottomSheetView = layoutInflater.inflate(R.layout.sheet_update_guru_name, null)
        val dialog = BottomSheetDialog(this)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.setContentView(bottomSheetView)
        bottomSheetView.btnUpdateName.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            binding.ivGuruImage.setImageURI(fileUri)

            val file: File = ImagePicker.getFile(data)!!
            val filePath: String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(ImagePicker.getError(data))
        } else {
            showToast("ImageChooser Task Cancelled")
        }
    }
}