package com.gabriel_codarcea.core.network.service

import com.gabriel_codarcea.core.data.model.Breed
import com.gabriel_codarcea.core.data.model.BreedURL
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("breeds/")
    fun getBreeds(@Header("x-api-key") apiKey: String): Call<List<Breed>>

    @GET("images/search")
    fun getImageForBreed(
        @Header("x-api-key") apiKey: String,
        @Query("breed_id") breed_id: String
    ): Call<List<BreedURL>>

}
