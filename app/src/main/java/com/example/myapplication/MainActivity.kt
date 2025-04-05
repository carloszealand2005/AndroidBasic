package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.api.frontend.SuperHeroListActivity
import com.example.myapplication.basics.Lifecycle
import com.example.myapplication.bmi.ImcActivity
import com.example.myapplication.datastore.SettingsActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.todo.TodoActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupListeners()
    }

    private fun setupListeners() {
        mBinding.buttonImc.setOnClickListener {
            val intent = Intent(this, ImcActivity::class.java)
            startActivity(intent)
        }
        mBinding.buttonTodoApp.setOnClickListener {
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)
        }
        mBinding.buttonPruebas.setOnClickListener {
            val intent = Intent(this, Lifecycle::class.java)
            startActivity(intent)
        }
        mBinding.buttonSuperHeroApp.setOnClickListener {
            val intent = Intent(this, SuperHeroListActivity::class.java)
            startActivity(intent)
        }
        mBinding.buttonSettingsApp.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


}