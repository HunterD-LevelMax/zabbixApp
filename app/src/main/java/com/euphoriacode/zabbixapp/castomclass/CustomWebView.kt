package com.euphoriacode.zabbixapp.castomclass

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar

class CustomWebView(context: Context, attrs: AttributeSet) : WebView(context, attrs) {
    private lateinit var progressBar: ProgressBar

    init {
        setWebContentsDebuggingEnabled(true)
        setupWebView()
    }

    fun setProgressBar(progressBar: ProgressBar) {
        this.progressBar = progressBar
    }

    fun setPcVersionMode(checked: Boolean) {
        this.settings.apply {
            if (checked) {
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                minimumFontSize = 10
                userAgentString =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537"
            } else {
                useWideViewPort = false
                loadWithOverviewMode = false
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false

                userAgentString = WebSettings.getDefaultUserAgent(context)
            }
        }
        this.reload()
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {

        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                this@CustomWebView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                this@CustomWebView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                // Обработка ошибки загрузки страницы
                Log.e("WebView Error", error?.description.toString())
            }
        }

        settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            cacheMode
            userAgentString = WebSettings.getDefaultUserAgent(context)
            allowContentAccess = true
            domStorageEnabled = true

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this@CustomWebView, true)
        }
    }

}