package com.gabriel_codarcea.core.network.`interface`

import com.gabriel_codarcea.core.data.model.Breed

interface ApiResponseInterface<T> {
    fun onFailure(t: Throwable)
    fun onResponse(breeds: List<Breed>)
}
