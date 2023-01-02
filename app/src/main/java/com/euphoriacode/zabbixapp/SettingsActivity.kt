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

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mySettings: Settings
    private var flag: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        loadData(storageDir)

        binding.apply {

            buttonSave.setOnClickListener {
                flag = saveData()
            }
        }
    }

    private fun saveData(): Boolean {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()

        binding.apply {
            if (globalEditText.text.toString() != "" || localEditText.text.toString() != "") {
                try {
                    saveFile(
                        localEditText.text.toString(),
                        globalEditText.text.toString(),
                        fileName,
                        storageDir
                    )
                    showToast("Saved successfully")
                } catch (e: Exception) {
                    showToast("Error")
                    e.printStackTrace()
                }
                return true
            } else {
                showToast("Enter urls in edit fields")
                    return false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (flag) {
            super.onBackPressed()
        }
    }

    private fun saveFile(local_ip: String, global_ip: String, nameFile: String, path: String) {
        val json = Gson().toJson(Settings(local_ip, global_ip))
        val file = File(path, nameFile)
        val output: Writer

        output = BufferedWriter(FileWriter(file))
        output.write(json.toString())
        output.close()
    }

    private fun loadData(storageDir: String) {
        try {
            mySettings = getSettings(storageDir)
            setDataEdit(mySettings)
            flag = true
        } catch (e: Exception) {
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