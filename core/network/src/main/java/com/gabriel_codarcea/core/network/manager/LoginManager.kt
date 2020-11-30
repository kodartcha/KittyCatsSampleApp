package com.gabriel_codarcea.core.network.manager

import com.gabriel_codarcea.core.network.`interface`.LoginResponseInterface
import com.gabriel_codarcea.core.network.client.LoginClient
import com.gabriel_codarcea.core.network.model.LoginResponse
import com.gabriel_codarcea.core.network.service.LoginService
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginManager : KoinComponent {

    private val loginClient: LoginClient by inject()
    private val loginService: LoginService? = loginClient.getClient()

    fun doLogin(user: String, pass: String, callback: LoginResponseInterface<LoginResponse>?) {
        loginService?.doLogin(user, pass)?.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                response.body()?.let { loginResponse ->
                    callback?.onResponse(loginResponse)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback?.onFailure(t)
            }
        })
    }
}
