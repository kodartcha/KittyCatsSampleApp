package com.gabriel_codarcea.kittycats.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.gabriel_codarcea.kittycats.R
import com.gabriel_codarcea.kittycats.databinding.ActivityLaunchBinding
import com.gabriel_codarcea.kittycats.model.ActivityType
import com.gabriel_codarcea.kittycats.viewmodel.LaunchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.thread

class LaunchActivity : AppCompatActivity() {

    private val viewModel by viewModel<LaunchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        initBindings()
        initObservers()

        supportActionBar?.hide()

        startWaitTime()
    }

    private fun startWaitTime() {
        viewModel.startBreedsDownload()
        thread(start = true) {
            Thread.sleep(2000)
            viewModel.startApp()
        }
    }

    private fun initBindings() {
        val binding: ActivityLaunchBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_launch
        )
        binding.lifecycleOwner = this
    }

    private fun initObservers() {
        viewModel.activityType.observe(this, { activityType ->
            when (activityType) {
                ActivityType.BREEDS_LIST -> navigateToBreedsListActivity()
                else -> navigateToLoginActivity()
            }
        })
    }

    private fun navigateToBreedsListActivity() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://breeds/breedsList"))
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://login/loginActivity"))
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        viewModel.cancelBreedsDownload()
        finish()
    }
}
