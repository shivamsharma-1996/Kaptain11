package com.shivam.kaptain11.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.shivam.kaptain11.R
import com.shivam.kaptain11.ui.guruDasboard.GuruDashboardActivity
import com.shivam.kaptain11.ui.makeAsGuru.MakeAsGuruActivity
import com.shivam.kaptain11.util.goToActivity

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
    }

    fun launchMakeAsGuru(view: View) {
        goToActivity(MakeAsGuruActivity::class.java)
    }
    fun launchGuruDashboard(view: View) {
        goToActivity(GuruDashboardActivity::class.java)
    }
}