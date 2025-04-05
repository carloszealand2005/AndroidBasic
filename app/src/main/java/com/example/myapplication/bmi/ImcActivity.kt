package com.example.myapplication.bmi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityImcBinding
import kotlin.math.pow

class ImcActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityImcBinding
    private var gender = 0 // 0 = male, 1 = female

    private var currentHeight = 120.0f
    private var currentWeight = 50
    private var currentAge = 30


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityImcBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupListeners()
    }

    private fun setupListeners() {
        mBinding.cardViewMan.setOnClickListener {
            gender = 0
            setBackgroundColor(gender)
        }
        mBinding.cardViewWoman.setOnClickListener {
            gender = 1
            setBackgroundColor(gender)
        }

        mBinding.rsHeight.addOnChangeListener { slider, value, fromUser ->
            mBinding.textViewHeight.text = "$value CM"
            currentHeight = value
        }

        mBinding.fabAddWeight.setOnClickListener {
            if(currentWeight >= 150) return@setOnClickListener
            currentWeight += 1
            updateWeight(currentWeight)
        }

        mBinding.fabSubstractWeight.setOnClickListener {
            if(currentWeight <= 25) return@setOnClickListener
            currentWeight -= 1
            updateWeight(currentWeight)
        }


        mBinding.fabAddAge.setOnClickListener {
            if(currentAge >= 105) return@setOnClickListener
            currentAge += 1
            updateAge(currentAge)
        }
        mBinding.fabSubstractAge.setOnClickListener {
            if(currentAge <= 0) return@setOnClickListener
            currentAge -= 1
            updateAge(currentAge)

        }


        mBinding.buttonSubmit.setOnClickListener {
            val bmi = calculateBMI()
            navigateToResult(bmi)
        }

    }

    private fun navigateToResult(bmi : Double) {
        val intent = Intent(this, ImcResultActivity::class.java)
        intent.putExtra(Tools.BMI_KEY, bmi)
        startActivity(intent)
    }


    private fun setBackgroundColor(gender : Int){

        if(gender == 0){ // Man
            mBinding.cardViewMan.setCardBackgroundColor(ContextCompat.getColor(this,
                R.color.background_component_selected
            ))
            mBinding.cardViewWoman.setCardBackgroundColor(ContextCompat.getColor(this,
                R.color.background_component
            ))
            return
        }
        // Girl
        mBinding.cardViewWoman.setCardBackgroundColor(ContextCompat.getColor(this,
            R.color.background_component_selected
        ))
        mBinding.cardViewMan.setCardBackgroundColor(ContextCompat.getColor(this,
            R.color.background_component
        ))
    }

    private fun updateWeight(weight : Int){
        mBinding.textViewWeight.text = "$weight KG"
    }

    private fun updateAge(age : Int){
        mBinding.textViewAge.text = "$age"
    }

    private fun calculateBMI(): Double {
        val heightInMeters = currentHeight / 100.0
        return currentWeight / heightInMeters.pow(2.0)
    }

}