package com.example.myapplication.api.frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.api.backend.apidatabase.ApiService
import com.example.myapplication.api.backend.apidatabase.SuperHeroItemResponse
import com.example.myapplication.api.backend.apidatabase.Tools
import com.example.myapplication.databinding.ActivitySuperHeroListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

class SuperHeroListActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivitySuperHeroListBinding
    private lateinit var retrofit: Retrofit
    private lateinit var superHeroAdapter : SuperHeroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySuperHeroListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBackend()
        setupComponents()
        setupListeners()
    }



    private fun setupListeners() {
        mBinding.searchViewSuperHero.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                val superHeroQuery = query.orEmpty().trim()
                if(superHeroQuery.isEmpty()){
                    Snackbar.make(mBinding.root, "Por favor ingrese un valor", Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(mBinding.root, "Buscando $superHeroQuery", Snackbar.LENGTH_SHORT).show()
                    searchByName(superHeroQuery)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun searchByName(query : String){
        mBinding.progressBar.visibility = View.VISIBLE

         CoroutineScope(Dispatchers.IO).launch {
             try {

                 val myResponse = retrofit.create(ApiService::class.java).getSuperHeroes(query)
                 if (!myResponse.isSuccessful || myResponse.body() == null) {
                     Log.i("BigoReport", "La respuesta no funciona ${myResponse.body()}")
                     return@launch
                 }

                 if (myResponse.body()!!.response == "error") {

                     Log.i("BigoReport", "No se encontraron resultados")
                     runOnUiThread {
                         Snackbar.make(
                             mBinding.root,
                             "No se encontraron resultados",
                             Snackbar.LENGTH_SHORT
                         ).show()
                         mBinding.progressBar.visibility = View.GONE
                     }
                     return@launch
                 }

                 val superHeroResults = myResponse.body()!!.results
                 superHeroResults.forEach {
                     Log.i(
                         "BigoReport",
                         "Superhero ID: ${it.name} con nombre ${it.name}, con img ${it.image}"
                     )
                 }
                 runOnUiThread {
                     mBinding.progressBar.visibility = View.GONE
                     renderResults(superHeroResults)
                 }
             }catch (e : IOException){
                 runOnUiThread {
                     Snackbar.make(mBinding.root, "Error in the request. Check your network connection", Snackbar.LENGTH_SHORT).show()
                     mBinding.progressBar.visibility = View.GONE
                 }
             }
         }
    }


    private fun setupComponents() {
        superHeroAdapter = SuperHeroAdapter(onItemSelected = { id -> navigateToDetail(id) })

        mBinding.recyclerViewSuperHero.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = superHeroAdapter
        }
    }

    private fun getRetrofit() : Retrofit {
       val retrofitConfig = Retrofit.Builder()
           .baseUrl(Tools.BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()

           return retrofitConfig
    }


    private fun setupBackend() { retrofit = getRetrofit() }

    private fun renderResults(superHeroResults : List<SuperHeroItemResponse>){
        superHeroAdapter.updateList(superHeroResults)
    }

    private fun navigateToDetail(id : String){
        val intent = Intent(this, DetailSuperHeroActivity::class.java)
        intent.putExtra(Tools.KEY_ID_SUPERHERO, id)
        intent.putExtra(Tools.KEY_ACTION, DetailAction.LOAD_FROM_API)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.superhero_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.superheroes_bookmark -> {
                val intent = Intent(this, SuperHeroSavedActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}