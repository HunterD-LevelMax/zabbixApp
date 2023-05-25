package com.euphoriacode.zabbixapp.activity

import android.os.Bundle
import android.os.Environment
import android.view.*
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import com.euphoriacode.zabbixapp.*
import com.euphoriacode.zabbixapp.castomclass.CustomWebView
import com.euphoriacode.zabbixapp.data.DataSettings
import com.euphoriacode.zabbixapp.databinding.ActivityWebBinding
import java.io.File

class WebActivity : AppCompatActivity() {

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
        webView = binding.webView
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
            "Dashboard panel global"
        } else {
            "Dashboard panel local"
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
                setPcVersionMode(item.isChecked)
                true
            }
            R.id.menu_via_btc ->{
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

    private fun setPcVersionMode(checked: Boolean) {
        webView.settings.apply {

            useWideViewPort = true
            loadWithOverviewMode = true

            userAgentString = if (checked) {
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (HTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"
            } else {
                ""
            }
        }
        webView.reload()
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




