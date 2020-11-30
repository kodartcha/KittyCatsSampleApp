package com.gabriel_codarcea.core.network.service

import com.gabriel_codarcea.core.network.model.LoginResponse
import com.gabriel_codarcea.core.network.model.RefreshResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {

    @GET("auth/login")
    fun doLogin(
        @Query("user") user: String,
        @Query("password") password: String
    ): Call<LoginResponse>

    @GET("auth/refresh")
    fun doRefreshToken(
        @Query("refresh_token") refresh_token: String
    ): Call<RefreshResponse>

}