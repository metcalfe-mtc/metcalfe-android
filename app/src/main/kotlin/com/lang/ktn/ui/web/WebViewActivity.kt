package com.lang.ktn.ui.web

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lang.ktn.base.BaseActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.nav_layout.*

class WebViewActivity : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        nav_tittle.setText("")
        nav_img_left.setOnClickListener{ finish() }
        init()
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {

        }
    }

    private val webClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }
    }




    private fun init() {
        var webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true// 设置允许访问文件数据
        webSettings.setSupportZoom(true)//支持缩放
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true

        webView!!.setWebViewClient(webClient)
        webView!!.setWebChromeClient(object : WebChromeClient(){
            override fun onReceivedTitle(view: WebView, title: String) {
                title?.let {
                    nav_tittle.setText(title)
                }
            }
        })


        var url=intent.getStringExtra("url")
        Log.e("url","url "+url)
        webView.loadUrl(url)
    }


}
