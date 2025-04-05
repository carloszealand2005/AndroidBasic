package com.example.myapplication.api.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.api.backend.apidatabase.SuperHeroImage
import com.example.myapplication.api.backend.apidatabase.SuperHeroItemResponse
import com.example.myapplication.api.backend.apidatabase.Tools
import com.example.myapplication.api.backend.localdatabase.DatabaseInit
import com.example.myapplication.api.backend.localdatabase.DatabaseInit.Companion.database
import com.example.myapplication.databinding.ActivitySuperHeroSavedBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SuperHeroSavedActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySuperHeroSavedBinding
    private lateinit var superHeroAdapter: SuperHeroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySuperHeroSavedBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        setupUI()
    }

    private fun setupUI() {
        CoroutineScope(Dispatchers.IO).launch {
            val superHeroes = getSavedSuperHeroes()
            runOnUiThread {
                setupRecyclerView(superHeroes)
            }
        }
    }

    private fun setupRecyclerView(superHeroes: List<SuperHeroItemResponse>) {
        superHeroAdapter = SuperHeroAdapter(superHeroes) { superheroId -> navigateToDetail(superheroId) }


        mBinding.recyclerViewSuperHeroSaved.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@SuperHeroSavedActivity, LinearLayoutManager.VERTICAL, false)
            adapter = superHeroAdapter
        }
    }


    /*
     Make a request to the superhero database and transform the data into a list of SuperHeroItemResponse
     NOTE: needs to be called on another thread than the main one
     */
    private fun getSavedSuperHeroes(): List<SuperHeroItemResponse> {
        val superHeroesSaved = database.getSuperHeroDao().getAllSuperHeroes()

        val superHeroList = superHeroesSaved.map { superhero ->
            SuperHeroItemResponse(
                id = superhero.id,
                name = superhero.name,
                image = SuperHeroImage(url = superhero.image)
            )
        }
        return superHeroList
    }

    private fun navigateToDetail(id : String){
        val intent = Intent(this, DetailSuperHeroActivity::class.java)
        intent.putExtra(Tools.KEY_ID_SUPERHERO, id)
        intent.putExtra(Tools.KEY_ACTION, DetailAction.LOAD_FROM_DATABASE)
        startActivity(intent)
    }
}

