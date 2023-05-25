package com.euphoriacode.zabbixapp

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import com.euphoriacode.zabbixapp.databinding.ActivityWebBinding
import java.io.File

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    private lateinit var webView: CustomWebView
    private lateinit var dataSettings: DataSettings
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            init()
            checkSettings()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        setTitle()
        webView = binding.webView
    }

    private var isGlobalUrl: Boolean = false

    private fun replaceUrl() {
        isGlobalUrl = !isGlobalUrl

        url = if (isGlobalUrl) {
            dataSettings.globalUrl
        } else {
            dataSettings.localIp
        }

        setTitle()
        webView.loadUrl(url)
    }

    private fun setTitle() {
        val title = if (isGlobalUrl) {
            "Dashboard panel global"
        } else {
            "Dashboard panel local"
        }
        supportActionBar?.title = title
    }

    private fun refreshWeb() {
        if (url.isNotEmpty()) {
            webView.loadUrl(url)
        } else {
            showToast("Enter URL in settings")
        }
    }

    private fun loadData() {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val filename = getString(R.string.filename)
        dataSettings = getSettings(storageDir.toString(), filename)
        url = dataSettings.localIp
        webView.loadUrl(url)
    }



    private fun setupFabButtons() {
        binding.fabRefresh.setOnClickListener {
            refreshWeb()
        }
        binding.fabReplaceUrl.setOnClickListener {
            replaceUrl()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                replaceActivity(SettingsActivity(), "no")
                true
            }
            R.id.exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cookieSave() {
        CookieManager.getInstance().apply {
            acceptCookie()
            flush() // сохранение cookies
        }
    }

    private fun checkSettings() {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val filename = getString(R.string.filename)
        val file = File(storageDir, filename)

        if (checkFile(file)) {
            loadData()
            setupFabButtons()
        } else {
            replaceActivity(SettingsActivity(), "no")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!getInternetStatus(this)) {
                showToast(getString(R.string.internet_status_message))
                return true
            }
            if (webView.canGoBack()) {
                webView.goBack()
                cookieSave()
                return true
            }
        }
        return false
    }

    override fun onStop() {
        cookieSave()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        try {
            checkSettings()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cookieSave()
        }
    }

    override fun onDestroy() {
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView.clearHistory()
        (webView.parent as ViewGroup).removeView(webView)
        webView.destroy()
        super.onDestroy()
    }
}




