package com.example.myapplication.datastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.datastore.Tools.BLUETOOTH_KEY
import com.example.myapplication.datastore.Tools.DARK_MODE_KEY
import com.example.myapplication.datastore.Tools.VIBRATION_KEY
import com.example.myapplication.datastore.Tools.VOLUME_KEY
import com.example.myapplication.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        renderUI()
        setupListeners()
    }




    private fun renderUI() {
        CoroutineScope(Dispatchers.IO).launch {
            val darkMode = Database.readBoolean(this@SettingsActivity, DARK_MODE_KEY)
            val bluetooth = Database.readBoolean(this@SettingsActivity, BLUETOOTH_KEY)
            val vibration = Database.readBoolean(this@SettingsActivity, VIBRATION_KEY, defaultValue = true)
            val volume = Database.readInt(this@SettingsActivity, VOLUME_KEY, defaultValue = 50)
            runOnUiThread {
                toggleNightMode(darkMode)
                mBinding.switchDarkMode.isChecked = darkMode
                mBinding.switchBluetooth.isChecked = bluetooth
                mBinding.switchVibration.isChecked = vibration
                mBinding.rangeSliderVolume.setValues(volume.toFloat())
            }
        }
    }


    private fun setupListeners() {
        mBinding.rangeSliderVolume.addOnChangeListener { _, value, _ ->
            mBinding.textViewVolume.text = "Volumen del dispositivo: ${value.toInt()}"
            CoroutineScope(Dispatchers.IO).launch {
                Database.writeInt(this@SettingsActivity, VOLUME_KEY, value.toInt())
            }
        }

        mBinding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                Database.writeBoolean(this@SettingsActivity, DARK_MODE_KEY, isChecked)
                CoroutineScope(Dispatchers.Main).launch { toggleNightMode(isChecked) }
            }
        }

        mBinding.switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                Database.writeBoolean(this@SettingsActivity, BLUETOOTH_KEY, isChecked)
            }
        }
        mBinding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                Database.writeBoolean(this@SettingsActivity, VIBRATION_KEY, isChecked)
            }
        }
    }

    private fun toggleNightMode(isEnabled: Boolean) {
        val mode = if (isEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode(mode)
        delegate.applyDayNight()
    }

}