package com.euphoriacode.zabbixapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

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

fun checkFile(file: File): Boolean {
    return file.exists() && !file.isDirectory
}

fun toGson(jsonString: String): DataSettings {
    val dataSettings = Gson().fromJson(jsonString, DataSettings::class.java)
    Log.d("result success", dataSettings.localIp + " " + dataSettings.globalUrl)
    return dataSettings
}

fun getJsonStringFromFile(storageDir: String, filename:String): String {
    return readFile("$storageDir/$filename", StandardCharsets.UTF_8)
}

fun getSettings(storageDir: String, filename: String): DataSettings {
    return toGson(getJsonStringFromFile(storageDir, filename))
}

fun getInternetStatus(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}