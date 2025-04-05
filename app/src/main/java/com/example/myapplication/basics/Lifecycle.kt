package com.example.myapplication.basics

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLifecycleBinding

class Lifecycle : AppCompatActivity() {
    private lateinit var mBinding: ActivityLifecycleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLifecycleBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        Log.i("BigoReport", "onCreate method called")
    }

    override fun onStart() {
        super.onStart()
        Log.i("BigoReport", "onStart method called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("BigoReport", "onResume method called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("BigoReport", "onPause method called")
    }
    override fun onStop() {
        super.onStop()
        Log.i("BigoReport", "onStop method called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("BigoReport", "onRestart method called")
    }


    override fun onDestroy() {
        Log.i("BigoReport", "onDestroy method called")
        super.onDestroy()

    }

}