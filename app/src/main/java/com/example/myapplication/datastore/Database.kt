package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.datastore.Tools.DATABASE_NAME
import kotlinx.coroutines.flow.first


object Database {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = DATABASE_NAME)



    suspend fun writeInt(context : Context, key : String, value : Int){
        context.dataStore.edit { database ->
            database[intPreferencesKey(key)] = value
        }
    }

    suspend fun writeBoolean(context : Context, key : String, value : Boolean){
        context.dataStore.edit { database ->
            database[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun readInt(context : Context, key : String, defaultValue : Int = 0) : Int {
        val intKey = intPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[intKey] ?: defaultValue
    }

    suspend fun readBoolean(context : Context, key : String, defaultValue : Boolean = false) : Boolean {
        val booleanKey = booleanPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[booleanKey] ?: defaultValue
    }
}