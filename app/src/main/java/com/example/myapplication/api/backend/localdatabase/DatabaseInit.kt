package com.example.myapplication.api.backend.localdatabase

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

class DatabaseInit : Application(){
    companion object{
        lateinit var database : SuperHeroDatabase
        const val DATABASE_NAME = "SuperHeroDatabase"
    }
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this,
            SuperHeroDatabase::class.java,
            DATABASE_NAME)
            .build()
    }

}


// Add converters of the class
@Database(entities = [SuperHeroEntity::class], version = 2)
@TypeConverters(SuperHeroTypeConverter::class)
abstract class SuperHeroDatabase : RoomDatabase(){
    abstract fun getSuperHeroDao() : SuperHeroDao
}
