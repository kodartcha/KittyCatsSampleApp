package com.gabriel_codarcea.core.network.client

import com.gabriel_codarcea.core.network.BuildConfig
import com.gabriel_codarcea.core.network.mocklogin.FakeInterceptor
import com.gabriel_codarcea.core.network.service.LoginService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoginClient {

    var loginService: LoginService? = null

    fun getClient(): LoginService? {
        if(loginService == null){
            //Fake interceptor until login server is completed
            val interceptor = FakeInterceptor()

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.LOGIN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            loginService = retrofit.create(LoginService::class.java)
        }
        return loginService
    }
}