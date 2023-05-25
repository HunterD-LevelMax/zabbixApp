package com.euphoriacode.zabbixapp.castomclass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class CustomActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


}