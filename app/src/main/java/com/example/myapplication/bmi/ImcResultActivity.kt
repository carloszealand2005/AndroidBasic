package com.example.myapplication.bmi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityResultImcBinding


class ImcResultActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityResultImcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityResultImcBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val bmi = intent.getDoubleExtra(Tools.BMI_KEY, -1.0)

        initResults(bmi)
        setupListeners(bmi)

    }

    private fun setupListeners(bmi : Double) {
        mBinding.buttonRecalculate.setOnClickListener { onBackPressed() }
        mBinding.buttonShare.setOnClickListener { shareBMI(bmi) }
    }



    private fun initResults(bmi: Double) {

        mBinding.tvResult.text = getResult(bmi)
        mBinding.tvResult.setTextColor(getColor(bmi))
        mBinding.tvBMI.text = java.text.DecimalFormat("#.##").format(bmi)
        mBinding.tvSuggest.text = getSuggest(bmi)
    }

    private fun getResult(bmi: Double) : String{
        return when {
            bmi < 13.0 -> "Desnutrición severa"
            bmi < 16.0 -> "Delgadez severa"
            bmi < 18.5 -> "Delgadez leve"
            bmi < 25.0 -> "Peso ideal"
            bmi < 27.5 -> "Sobrepeso leve"
            bmi < 30.0 -> "Sobrepeso moderado"
            bmi < 35.0 -> "Obesidad moderada"
            else -> "Obesidad severa"
        }
    }
    private fun getSuggest(bmi: Double): String {
        return when {
            bmi < 13.0 -> "Estás en un estado de desnutrición. Consulta a un profesional de la salud."
            bmi < 16.0 -> "Estás en un estado de delgadez severa. Es recomendable mejorar tu nutrición."
            bmi < 18.5 -> "Estás en un estado de delgadez leve. Podrías considerar mejorar tu dieta."
            bmi < 19.5 -> "Estás dentro de un rango saludable pero considerablemente delgado"
            bmi < 25.0 -> "Estás dentro de un rango saludable, sigue así"
            bmi < 27.5 -> "Estás un poco por encima de tu peso ideal"
            bmi < 30.0 -> "Estás considerablemente encima de tu peso ideal. Considera mejorar tu dieta"
            bmi < 35.0 -> "Estás en estado de obesidad moderada. Consulta a un profesional de la salud"
            else -> "Estás en estado de obesidad severa. Consulta a un profesional de la salud"
        }
    }

    private fun getColor(bmi : Double) : Int {
        return when {
            bmi < 18.5 -> ContextCompat.getColor(this, R.color.color_wrong)
            bmi < 25.0 -> ContextCompat.getColor(this, R.color.color_right)
            else -> ContextCompat.getColor(this, R.color.color_very_wrong)
        }
    }

    private fun shareBMI(bmi: Double) {

         val bmiFormatted = java.text.DecimalFormat("#.##").format(bmi)
         val textShare = "¡Mi indice de masa corporal es de $bmiFormatted!\n Esto significa que tengo ${getResult(bmi)}!"
         val shareIntent = Intent(Intent.ACTION_SEND).apply {
             type = "text/plain"
             putExtra(Intent.EXTRA_TEXT, textShare)
         }
        startActivity(Intent.createChooser(shareIntent, "Share result"))


    }
}