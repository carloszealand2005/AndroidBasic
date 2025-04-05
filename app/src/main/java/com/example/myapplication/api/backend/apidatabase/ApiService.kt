package com.example.myapplication.api.backend.apidatabase

import com.example.myapplication.api.backend.apidatabase.Tools.ACCESS_TOKEN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Interface to consume superhero API
interface ApiService {

    @GET("/api/$ACCESS_TOKEN/search/{name}")
    suspend fun getSuperHeroes(@Path("name") superheroName: String) : Response<SuperHeroDataResponse>

    @GET("/api/$ACCESS_TOKEN/{id}")
    suspend fun getSuperHeroById(@Path("id") superheroID : String) : Response<SuperHeroDetailResponse>
}