package com.example.myapplication.api.backend.localdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SuperHeroDao {
    @Query("SELECT * FROM superhero")
    fun getAllSuperHeroes() : List<SuperHeroEntity>


    @Query("SELECT * FROM superhero WHERE id = :id")
    fun getSuperHeroById(id : String) : SuperHeroEntity?


    @Query("SELECT image FROM superhero WHERE id = :id")
    fun getSuperHeroImage(id : Long) : String

    @Delete
    fun deleteSuperHero(superHeroEntity: SuperHeroEntity)

    @Query("DELETE FROM superhero WHERE id = :id")
    fun deleteSuperHeroById(id : String)

    @Query("DELETE FROM superhero WHERE name = :name")
    fun deleteSuperHeroByName(name : String)

    //En caso de llave primaria duplicada, ignorar la inserción
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSuperHero(superHeroEntity: SuperHeroEntity)


}