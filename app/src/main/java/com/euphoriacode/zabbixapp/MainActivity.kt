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

        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + fileName)

        try {
            if (checkFile(file)) {
                replaceActivity(WebActivity())
                Log.d("File: ", "exist")
            } else {
                replaceActivity(SettingsActivity())
                showToast("Enter new ip addresses")
                Log.d("File: ", "no exist")
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun checkFile(file: File): Boolean {
        return file.exists() && !file.isDirectory
    }

}


