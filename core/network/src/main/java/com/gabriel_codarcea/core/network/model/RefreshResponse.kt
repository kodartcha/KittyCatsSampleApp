package com.gabriel_codarcea.core.network.model

import com.google.gson.annotations.SerializedName

data class RefreshResponse (
    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("auth_token")
    var authToken: String,

    @SerializedName("refresh_token")
    var refreshToken: String,
)