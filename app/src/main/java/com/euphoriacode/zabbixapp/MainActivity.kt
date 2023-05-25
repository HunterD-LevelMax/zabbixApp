package com.euphoriacode.zabbixapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkSettings()
    }

    private fun checkSettings() {
        val file =
            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + getString(R.string.filename))
        try {
            if (checkFile(file)) {
                replaceActivity(WebActivity())
            } else {
                showToast("Enter urls")
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }
}


