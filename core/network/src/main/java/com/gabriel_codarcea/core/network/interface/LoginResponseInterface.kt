package com.gabriel_codarcea.core.network.`interface`

import com.gabriel_codarcea.core.network.model.LoginResponse

interface LoginResponseInterface<T> {
    fun onFailure(t: Throwable)
    fun onResponse(response: LoginResponse)
}
