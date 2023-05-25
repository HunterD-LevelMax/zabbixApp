package com.euphoriacode.zabbixapp.activity

import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import com.euphoriacode.zabbixapp.R
import com.euphoriacode.zabbixapp.castomclass.CustomActivity
import com.euphoriacode.zabbixapp.data.DataSettings
import com.euphoriacode.zabbixapp.databinding.ActivitySettingsBinding
import com.euphoriacode.zabbixapp.getSettings
import com.euphoriacode.zabbixapp.showToast
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer

class SettingsActivity : CustomActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var myDataSettings: DataSettings
    private var dataSave: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()

        try {
            loadData(storageDir)
        }catch (e:Exception){
            binding.globalEditText.setText("https://job.3err0.ru/zabbix.php?action=dashboard.view")
            binding.localEditText.setText("http://10.1.0.10")
            e.printStackTrace()
        }

        binding.apply {
            buttonSave.setOnClickListener {
                dataSave = saveData()
            }
        }
    }

    private fun saveData(): Boolean {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()

        binding.apply {
            if (globalEditText.text.isNotEmpty() || localEditText.text.isNotEmpty()) {
                try {
                    saveFile(
                        localEditText.text.toString(),
                        globalEditText.text.toString(),
                        getString(R.string.filename),
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
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && dataSave) {
            onBackPressedDispatcher.onBackPressed()
                return true
        }
        return false
    }

    private fun saveFile(local_ip: String, global_ip: String, nameFile: String, path: String) {
        val json = Gson().toJson(DataSettings(local_ip, global_ip))
        val file = File(path, nameFile)
        val output: Writer

        output = BufferedWriter(FileWriter(file))
        output.write(json.toString())
        output.close()
    }

    private fun loadData(storageDir: String) {
            myDataSettings = getSettings(storageDir, getString(R.string.filename))
            setDataEdit(myDataSettings)
            dataSave = true
    }

    private fun setDataEdit(dataSettings: DataSettings) {
        binding.apply {
            localEditText.setText(dataSettings.localIp)
            globalEditText.setText(dataSettings.globalUrl)
        }
    }

}