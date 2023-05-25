package com.euphoriacode.zabbixapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebView(context: Context, attrs: AttributeSet) : WebView(context, attrs) {

    init {
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webViewClient = WebViewClient()
        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        settings.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            allowContentAccess = true
            userAgentString = ""
            domStorageEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            loadsImagesAutomatically = true
        }
    }
}