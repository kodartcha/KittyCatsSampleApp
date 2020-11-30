package com.gabriel_codarcea.features.login.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabriel_codarcea.core.data.model.LoadingState
import com.gabriel_codarcea.core.data.preferences.SharedPreferencesManager
import com.gabriel_codarcea.core.network.`interface`.LoginResponseInterface
import com.gabriel_codarcea.core.network.manager.BreedsManager
import com.gabriel_codarcea.core.network.manager.LoginManager
import com.gabriel_codarcea.core.network.model.LoginResponse
import com.gabriel_codarcea.core.network.utils.LOGIN_RESPONSE_CODE_SUCCESS
import com.gabriel_codarcea.features.login.model.LoginError
import com.gabriel_codarcea.features.login.utils.isValidEmail
import com.gabriel_codarcea.features.login.utils.isValidPassword
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginViewModel : ViewModel(), KoinComponent {

    val loginState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.EMPTY)
    val loginError: MutableLiveData<LoginError> = MutableLiveData()

    fun state(): LiveData<LoadingState> = loginState

    private val loginManager: LoginManager by inject()
    private val sharedPrefs: SharedPreferencesManager by inject()
    private val breedsManager: BreedsManager by inject()

    private var userEmail: String = ""
    private var userPassword: String = ""

    fun startLogin() {
        loginState.value = LoadingState.IN_PROGRESS

        if (!isValidEmail(userEmail)) {
            loginError.value = LoginError.INVALID_EMAIL
            loginState.value = LoadingState.ERROR
            return
        }
        if (!isValidPassword(userPassword)) {
            loginError.value = LoginError.INVALID_PASSWORD
            loginState.value = LoadingState.ERROR
            return
        }

        loginManager.doLogin(
            userEmail,
            userPassword,
            object : LoginResponseInterface<LoginResponse> {
                override fun onResponse(response: LoginResponse) {
                    when (response.statusCode) {
                        LOGIN_RESPONSE_CODE_SUCCESS -> {
                            if (response.authToken.isNotEmpty() && response.refreshToken.isNotEmpty()) {
                                sharedPrefs.setLoggedIn(true)
                                sharedPrefs.saveAuthToken(response.authToken)
                                sharedPrefs.saveRefreshToken(response.refreshToken)
                                sharedPrefs.saveUser(
                                    response.user.firstName,
                                    response.user.lastName,
                                    response.user.id,
                                    response.user.email
                                )

                                loginError.value = LoginError.NONE
                                loginState.value = LoadingState.FINISHED
                            } else {
                                loginError.value = LoginError.REQUEST_ERROR
                                loginState.value = LoadingState.ERROR
                            }
                        }
                        else -> {
                            loginError.value = LoginError.INVALID_CREDENTIALS
                            loginState.value = LoadingState.ERROR
                        }
                    }
                }

                override fun onFailure(t: Throwable) {
                    loginError.value = LoginError.REQUEST_ERROR
                    loginState.value = LoadingState.ERROR
                }
            })
    }

    fun cancelBreedsDownload() {
        breedsManager.cancelDownload()
    }

    val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            userEmail = s.toString()
        }

        override fun afterTextChanged(s: Editable) {}
    }

    val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            userPassword = s.toString()
        }

        override fun afterTextChanged(s: Editable) {}
    }
}
