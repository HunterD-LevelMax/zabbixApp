package com.euphoriacode.zabbixapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
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
        supportActionBar?.title = "Dashboard panel"

        init()
        loadData()

        loadUrl(url)
        cookieSave()

        binding.apply {
            fabRefresh.setOnClickListener {
                webView.loadUrl(url)
            }

            fabReplaceUrl.setOnClickListener {
                count++
                if (count == 1) {
                    url = mySettings.globalUrl
                    showToast("Use global url")
                } else {
                    url = mySettings.localIp
                    showToast("Use local url")
                    count = 0
                }
            }
        }
    }

    private fun loadData() {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        try {
            mySettings = getSettings(storageDir)
            url = mySettings.localIp
        } catch (e: Exception) {
            showToast("error")
            e.printStackTrace()
        }
    }

    private fun init() {
        webView = binding.webView
    }

    private fun loadUrl(url: String) {
        setupWebView()
        webView.loadUrl(url)
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
        }
        return true
    }

    private fun cookieSave() {
        CookieManager.getInstance().acceptCookie()
        CookieManager.getInstance().flush() // сохранение cookies
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            CookieManager.getInstance().flush()
        } else {
            CookieManager.getInstance().flush()
        }
    }

    override fun onStop() {
        CookieManager.getInstance().flush()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        CookieManager.getInstance().flush()
    }

    override fun onDestroy() {
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView.clearHistory()
        (webView.parent as ViewGroup).removeView(webView)
        webView.destroy()
        super.onDestroy()
    }
}




