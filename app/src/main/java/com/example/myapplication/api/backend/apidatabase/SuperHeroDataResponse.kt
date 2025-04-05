package com.example.myapplication.api.backend.apidatabase

import com.google.gson.annotations.SerializedName

data class SuperHeroDataResponse(
    @SerializedName ("response") val response : String,
    @SerializedName ("results") val results : List<SuperHeroItemResponse>
)

data class SuperHeroItemResponse(
    @SerializedName ("id") val id : String,
    @SerializedName ("name") val name : String,
    @SerializedName ("image") val image : SuperHeroImage
)

data class SuperHeroImage(
    @SerializedName ("url") val url : String
)

