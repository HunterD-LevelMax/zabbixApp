package com.euphoriacode.zabbixapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.euphoriacode.zabbixapp.databinding.ActivityWebBinding
import java.io.File

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    private lateinit var webView: WebView
    private lateinit var mySettings: Settings
    private lateinit var url: String
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        checkSettings()
        cookieSave()

        binding.apply {
            fabRefresh.setOnClickListener {
                try {
                    refreshWeb()
                } catch (e: Exception) {
                    showToast("Error")
                    e.printStackTrace()
                }
            }

            fabReplaceUrl.setOnClickListener {
                try {
                    replaceUrl()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun init() {
        setTitle()
        webView = binding.webView
    }

    private fun replaceUrl() {
        count++
        if (count == 1) {
            url = mySettings.globalUrl
            loadData()
        } else {
            url = mySettings.localIp
            loadData()
            setTitle()
            count = 0
        }
        setTitle()
    }

    private fun refreshWeb() {
        if (url != "") {
            webView.loadUrl(url)
        } else {
            showToast("Enter url in settings")
        }
    }


    private fun loadData() {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        try {
            mySettings = getSettings(storageDir)
            url = mySettings.localIp
            loadUrl(url)
        } catch (e: Exception) {
            showToast("edit urls settings")
            e.printStackTrace()
        }
    }

    private fun loadUrl(url: String) {
        setupWebView()
        webView.loadUrl(url)
    }

    private fun setTitle() {
        if (count == 0) {
            supportActionBar?.title = "Dashboard panel local"
        } else {
            supportActionBar?.title = "Dashboard panel global"
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.webViewClient = WebViewClient()
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                cacheMode
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                allowContentAccess = true
                userAgentString
                domStorageEnabled = true
                allowUniversalAccessFromFileURLs
                builtInZoomControls = true
                displayZoomControls = false
                loadsImagesAutomatically = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> replaceActivity(SettingsActivity(), "no")
            R.id.exit -> finish()
        }
        return true
    }

    private fun cookieSave() {
        CookieManager.getInstance().acceptCookie()
        CookieManager.getInstance().flush() // сохранение cookies
    }

    private fun checkSettings() {
        val file =
            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + fileName)
        try {
            if (checkFile(file)) {
                loadData()
                Log.d("File: ", "exist")
            } else {
                replaceActivity(SettingsActivity(), "no")
                showToast("Enter new ip addresses")
                Log.d("File: ", "no exist")
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
        cookieSave()
    }

    override fun onStop() {
        cookieSave()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        cookieSave()
    }

    override fun onDestroy() {
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView.clearHistory()
        (webView.parent as ViewGroup).removeView(webView)
        webView.destroy()
        super.onDestroy()
    }
}




