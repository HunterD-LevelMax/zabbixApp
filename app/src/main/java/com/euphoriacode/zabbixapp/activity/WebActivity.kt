package com.euphoriacode.zabbixapp.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.euphoriacode.zabbixapp.*
import com.euphoriacode.zabbixapp.castomclass.CustomWebView
import com.euphoriacode.zabbixapp.data.DataSettings
import com.euphoriacode.zabbixapp.databinding.ActivityWebBinding
import java.io.File

class WebActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var binding: ActivityWebBinding
    private lateinit var webView: CustomWebView
    private lateinit var dataSettings: DataSettings
    private lateinit var url: String
    private var isGlobalUrl: Boolean = false

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
        progressBar = binding.progressBar
        webView = binding.webView
        webView.setProgressBar(progressBar)
    }

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
            getString(R.string.global_url_dashboard)
        } else {
            getString(R.string.local_url_dasboard)
        }
        supportActionBar?.title = title
    }

    private fun refreshWeb() {
        webView.reload()
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
            R.id.menu_pc_version -> {
                item.isChecked = !item.isChecked
                setPcMode(item.isChecked)
                showToast("Doesn't work any websites")
                true
            }
            R.id.menu_via_btc -> {
                webView.loadUrl(via_url)
                true
            }
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setPcMode(checked: Boolean) {
        webView.setPcVersionMode(checked)
    }

    private fun saveCookies() {
        CookieManager.getInstance().apply {
            acceptCookie()
            flush()
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
                saveCookies()
                return true
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return false
    }

    override fun onStop() {
        saveCookies()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        try {
            checkSettings()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            setTitle()
            saveCookies()
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




