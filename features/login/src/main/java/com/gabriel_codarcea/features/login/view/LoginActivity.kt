package com.gabriel_codarcea.features.login.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.gabriel_codarcea.core.data.model.LoadingState
import com.gabriel_codarcea.features.login.R
import com.gabriel_codarcea.features.login.databinding.ActivityLoginBinding
import com.gabriel_codarcea.features.login.model.LoginError
import com.gabriel_codarcea.features.login.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModel<LoginViewModel>()

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
    }

    private fun initBindings() {
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initObservers() {
        viewModel.loginState.observe(this, { state ->
            when (state) {
                LoadingState.EMPTY -> {
                }
                LoadingState.IN_PROGRESS -> {
                }
                LoadingState.FINISHED -> {
                    navigateToBreedsListActivity()
                }
                LoadingState.ERROR -> {
                }
                else -> {
                }
            }
        })

        viewModel.loginError.observe(this, { error ->
            when (error) {
                LoginError.INVALID_EMAIL -> loginError.text =
                    resources.getString(R.string.invalid_username)
                LoginError.INVALID_PASSWORD -> loginError.text =
                    resources.getString(R.string.invalid_password)
                LoginError.INVALID_CREDENTIALS -> loginError.text =
                    resources.getString(R.string.invalid_credentials)
                LoginError.REQUEST_ERROR -> loginError.text =
                    resources.getString(R.string.request_error)
                else -> loginError.text = ""
            }
        })
    }

    private fun navigateToBreedsListActivity() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://breeds/breedsList"))
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        viewModel.cancelBreedsDownload()
        finish()
    }
}
