package com.example.myapplication.api.backend.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.myapplication.api.backend.apidatabase.SuperHeroAppearance
import com.example.myapplication.api.backend.apidatabase.SuperHeroBiography
import com.example.myapplication.api.backend.apidatabase.SuperHeroPowerStats
import com.google.gson.Gson

@Entity("superhero")
data class SuperHeroEntity(
    @PrimaryKey var id: String = "",
    val name: String,
    val powerstats: SuperHeroPowerStats, //JSON
    val biography: SuperHeroBiography, // JSON
    val appearance: SuperHeroAppearance, // JSON
    val image: String // Not a JSON, will directly get from the request.
)



class SuperHeroTypeConverter{
    private val gson = Gson()
    @TypeConverter
    fun fromPowerStats(powerStats : SuperHeroPowerStats) = gson.toJson(powerStats)

    @TypeConverter
    fun toPowerStats(powerStatsJson : String) = gson.fromJson(powerStatsJson, SuperHeroPowerStats::class.java)


    @TypeConverter
    fun fromBiography(biography : SuperHeroBiography) = gson.toJson(biography)

    @TypeConverter
    fun toBiography(biographyJson : String) = gson.fromJson(biographyJson, SuperHeroBiography::class.java)


    @TypeConverter
    fun fromAppearance(appearance : SuperHeroAppearance) = gson.toJson(appearance)

    @TypeConverter
    fun toAppearance(appearanceJson : String) = gson.fromJson(appearanceJson, SuperHeroAppearance::class.java)




}