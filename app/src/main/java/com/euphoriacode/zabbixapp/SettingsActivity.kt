package com.euphoriacode.zabbixapp

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.euphoriacode.zabbixapp.databinding.ActivitySettingsBinding
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer


class SettingsActivity : AppCompatActivity() {
    private lateinit var mySettings: Settings

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()

        loadData(storageDir)

        binding.apply {
            buttonSave.setOnClickListener {
                try {
                    saveData(localUrl, globalUrl, fileName, storageDir)

                    // saveData(localEditText.text.toString(), globalEditText.text.toString(), fileName, storageDir)
                    showToast("Saved successfully")
                } catch (e: Exception) {
                    showToast("Error")
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun saveData(local_ip: String, global_ip: String, nameFile: String, path: String) {
        var json = Gson().toJson(Settings(local_ip, global_ip))
        val file = File(path, nameFile)
        val output: Writer

        output = BufferedWriter(FileWriter(file))
        output.write(json.toString())
        output.close()
    }

    private fun loadData(storageDir: String) {
        try {
            mySettings = toGson(getJsonFromFile(storageDir))
            setDataEdit(mySettings)
        } catch (e: Exception) {
            showToast("error")
            e.printStackTrace()
        }
    }

    private fun setDataEdit(settings: Settings) {
        binding.apply {
            localEditText.setText(settings.localIp)
            globalEditText.setText(settings.globalUrl)
        }
    }

}