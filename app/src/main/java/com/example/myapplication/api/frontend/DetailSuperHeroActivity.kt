package com.example.myapplication.api.frontend

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.api.backend.apidatabase.ApiService
import com.example.myapplication.api.backend.apidatabase.SuperHeroAppearance
import com.example.myapplication.api.backend.apidatabase.SuperHeroBiography
import com.example.myapplication.api.backend.apidatabase.SuperHeroDetailResponse
import com.example.myapplication.api.backend.apidatabase.SuperHeroImage
import com.example.myapplication.api.backend.apidatabase.SuperHeroPowerStats
import com.example.myapplication.api.backend.apidatabase.Tools
import com.example.myapplication.api.backend.apidatabase.toSuperHeroDetailEntity
import com.example.myapplication.api.backend.localdatabase.DatabaseInit
import com.example.myapplication.api.backend.localdatabase.SuperHeroEntity
import com.example.myapplication.databinding.ActivityDetailSuperHeroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

enum class DetailAction{
    LOAD_FROM_API,
    LOAD_FROM_DATABASE
}
class DetailSuperHeroActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDetailSuperHeroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDetailSuperHeroBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val id = intent.extras?.getString(Tools.KEY_ID_SUPERHERO).orEmpty()

        val action = intent?.extras?.getSerializable(Tools.KEY_ACTION)
                as? DetailAction? ?: DetailAction.LOAD_FROM_API


            lifecycleScope.launch {
                val superhero = when(action){
                    DetailAction.LOAD_FROM_API -> {
                        mBinding.floatingActionButtonBookmark.visibility = View.VISIBLE
                        loadSuperheroFromApiById(id)
                    }
                    DetailAction.LOAD_FROM_DATABASE -> {
                        mBinding.floatingActionButtonRemove.visibility = View.VISIBLE
                        loadSuperheroFromLocalById(id)
                    }
                }

                superhero?.let {
                    renderUI(it)
                    setupListeners(it, id)

                } ?: run {
                    Snackbar.make(mBinding.root, "Error al cargar la vista", Snackbar.LENGTH_SHORT).show()
                }
            }


    }



    private fun setupListeners(superhero : SuperHeroDetailResponse, superheroId : String) {
        mBinding.floatingActionButtonBookmark.setOnClickListener {
                saveSuperHero(superhero)
        }

        mBinding.floatingActionButtonRemove.setOnClickListener {
                deleteSuperHero(superheroId)
        }
    }

    private fun deleteSuperHero(superheroId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseInit.database.getSuperHeroDao().deleteSuperHeroById(superheroId)
        }
        Toast.makeText(this, "Superhero deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun saveSuperHero(superHero: SuperHeroDetailResponse) {

        CoroutineScope(Dispatchers.IO).launch {
            DatabaseInit.database.getSuperHeroDao().insertSuperHero(superHero.toSuperHeroDetailEntity())
            runOnUiThread {
                Snackbar.make(mBinding.root, "${superHero.name} guardado", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private suspend fun loadSuperheroFromApiById(id: String): SuperHeroDetailResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val myResponse = getRetrofit().create(ApiService::class.java).getSuperHeroById(id)
                if (!myResponse.isSuccessful || myResponse.body() == null) {
                    runOnUiThread {
                        Log.i("BigoReport", "La respuesta no funciona ${myResponse.body()}")
                        Snackbar.make(mBinding.root, "Error al cargar la vista", Snackbar.LENGTH_SHORT).show()
                        mBinding.progressBarDetail.visibility = View.GONE
                    }
                    return@withContext null
                }

                if (myResponse.body()!!.response == "error") {
                    runOnUiThread {
                        Log.i("BigoReport", "No se encontraron resultados")
                        Snackbar.make(mBinding.root, "No se encontraron resultados", Snackbar.LENGTH_SHORT).show()
                        mBinding.progressBarDetail.visibility = View.GONE
                    }
                    return@withContext null
                }

                myResponse.body()!!
            } catch (e: Exception) {
                runOnUiThread {
                    Snackbar.make(mBinding.root, "Error en la peticion $e", Snackbar.LENGTH_SHORT).show()
                    mBinding.progressBarDetail.visibility = View.GONE
                }
                return@withContext null
            }
        }
    }

    private suspend fun loadSuperheroFromLocalById(id: String): SuperHeroDetailResponse? {
        return withContext(Dispatchers.IO){
            val superhero = DatabaseInit.database.getSuperHeroDao().getSuperHeroById(id)

            //Parse data type to JSON:
            superhero?.let {

                SuperHeroDetailResponse(
                    response = "success",
                    id = superhero.id,
                    name = superhero.name,
                    powerstats = superhero.powerstats,
                    biography = superhero.biography,
                    appearance = superhero.appearance,
                    image = SuperHeroImage(url = superhero.image))
            }
        }
    }

    private fun renderUI(superHero: SuperHeroDetailResponse) {

        Picasso.get().load(superHero.image.url).into(mBinding.imageViewSuperheroDetail)
        mBinding.textViewSuperHeroDetail.text = superHero.name
        mBinding.textViewSuperHeroFullName.text = superHero.biography.fullName.ifEmpty{
            "No full name available" }


        // Update text
        mBinding.textViewIntelligence.text = "Inteligencia:" + superHero.powerstats.intelligence.ifBlank { "No available" }
        mBinding.textViewPower.text = "Poder: " + superHero.powerstats.power
        mBinding.textViewCombat.text = "Combate: " + superHero.powerstats.combat
        mBinding.textViewSpeed.text = "Velocidad: " + superHero.powerstats.speed

        // Update views
        if(superHero.powerstats.intelligence != "null"){
            updateWidth(mBinding.viewIntelligence, superHero.powerstats.intelligence)
        }

        if(superHero.powerstats.power != "null"){
            updateWidth(mBinding.viewPower, superHero.powerstats.power)
        }

        if(superHero.powerstats.combat != "null"){
            updateWidth(mBinding.viewCombat, superHero.powerstats.combat)
        }

        if(superHero.powerstats.speed != "null"){
            updateWidth(mBinding.viewSpeed, superHero.powerstats.speed)
        }



    }

    private fun updateWidth(view : View, newWidth : String){
        val params = view.layoutParams
        params.width = pixelToDp(newWidth.toFloat())
        view.layoutParams = params
    }

    private fun pixelToDp(px : Float) : Int =
       TypedValue
           .applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics)
           .roundToInt() * 2



    private fun getRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(Tools.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}