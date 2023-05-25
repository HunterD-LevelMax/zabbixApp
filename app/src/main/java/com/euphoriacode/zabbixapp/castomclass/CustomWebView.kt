package com.euphoriacode.zabbixapp.castomclass

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebView(context: Context, attrs: AttributeSet) : WebView(context, attrs) {

    init {
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webViewClient = WebViewClient()

        settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            cacheMode
           // userAgentString =  "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0"
            allowContentAccess = true
            setSupportMultipleWindows(true)
            domStorageEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this@CustomWebView, true)
        }
    }
}