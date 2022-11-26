package com.euphoriacode.zabbixapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

const val localUrl = "http://10.1.0.10"
const val globalUrl = "https://job.3err0.ru/zabbix.php?action=dashboard.view"
const val urlGoogle = "https://www.google.com"
const val fileName = "My settings.json"

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity, noFinish: String) {
    val intent = Intent(this, activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity, name: String, url: String) {
    val intent = Intent(this, activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.putExtra(name, url)
    startActivity(intent)
    this.finish()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun readFile(path: String, encoding: Charset): String {
    return Files.readAllLines(Paths.get(path), encoding)[0]
}

private fun createFile(name: String, paths: String): File {
    return File(paths, name)
}

fun toGson(jsonString: String): Settings {
    val settings = Gson().fromJson(jsonString, Settings::class.java)
    Log.d("result success", settings.localIp + " " + settings.globalUrl)
    return settings
}

fun getJsonStringFromFile(storageDir: String): String {
    Log.d("json", readFile("$storageDir/$fileName", StandardCharsets.UTF_8))
    return readFile("$storageDir/$fileName", StandardCharsets.UTF_8)
}

fun getSettings(storageDir: String): Settings {
    return toGson(getJsonStringFromFile(storageDir))
}