package com.example.myapplication.api.backend.apidatabase

import com.google.gson.annotations.SerializedName

data class SuperHeroDetailResponse(
    @SerializedName("response") val response : String,
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("powerstats") val powerstats : SuperHeroPowerStats,
    @SerializedName("biography") val biography : SuperHeroBiography,
    @SerializedName("appearance") val appearance : SuperHeroAppearance,
    @SerializedName("image") val image : SuperHeroImage
)



data class SuperHeroPowerStats(
    @SerializedName("intelligence") val intelligence : String,
    @SerializedName("power") val power : String,
    @SerializedName("combat") val combat : String,
    @SerializedName("speed") val speed : String
)

data class SuperHeroBiography(
    @SerializedName("full-name") val fullName : String,
    @SerializedName("place-of-birth") val placeOfBirth : String
)

data class SuperHeroAppearance(
    @SerializedName("gender") val gender : String,
    @SerializedName("race") val race : String,
    @SerializedName("height") val height : List<String>,
    @SerializedName("weight") val weight : List<String>
)
