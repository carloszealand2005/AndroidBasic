package com.example.myapplication.api.backend.apidatabase

import com.example.myapplication.api.backend.localdatabase.SuperHeroEntity

object Tools {
    const val BASE_URL = "https://superheroapi.com/api/"
    const val ACCESS_TOKEN = "1a954f1fe42bde57a972a22e3490093d"
    const val KEY_ID_SUPERHERO = "superhero_key"
    const val KEY_ACTION = "key_action"
}


//Extension functions to convert from API object to LOCAL DATABASE object back and forth

fun SuperHeroDetailResponse.toSuperHeroDetailEntity() : SuperHeroEntity {
    return SuperHeroEntity(
        id = this.id,
        name = this.name,
        powerstats = this.powerstats,
        biography = this.biography,
        appearance = this.appearance,
        image = this.image.url
    )
}





