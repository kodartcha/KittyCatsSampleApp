package com.gabriel_codarcea.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "breeds")
data class Breed(
    @PrimaryKey(autoGenerate = true) @SerializedName("dbID") val dbID: Int = 0,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("temperament") val temperament: String?,
    @SerializedName("country_code") val country_code: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("wikipedia_url") val wikipedia_url: String?,
    @SerializedName("origin") val origin: String?,
    @SerializedName("image") var image: String? = null
)
