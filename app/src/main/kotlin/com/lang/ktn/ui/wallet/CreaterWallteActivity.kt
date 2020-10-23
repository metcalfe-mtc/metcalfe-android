package com.lang.ktn.ui.wallet

import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.TextView
import androidx.room.Room
import com.google.gson.Gson
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.WalletBean
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.ui.home.HomeActivity
import com.lang.ktn.ui.main.MainActivity
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.ktn.utils.AesEBC
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.changeEyes
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_create_wallet.*
import kotlinx.android.synthetic.main.fragment_zhujici.*
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.logging.Logger

class CreaterWallteActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_wallet)
        nav_tittle.setText(R.string.create_wallet_title)
        nav_img_left.setOnClickListener{ finish() }
        initWebView()

        lookPwd_create1.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_psd1,isChecked) }
        lookPwd2_create2.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_psd2,isChecked) }

        edt_name.onCreateWallte()
        edt_psd1.onCreateWallte()
        edt_psd2.onCreateWallte()




        launch(Dispatchers.IO) {
            var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
            var listAccount = accountTable.accountDao.queryAllUserInfo()
            withContext(Dispatchers.Main) {
                if (listAccount != null && listAccount.size > 0) {
                    edt_name.setText(String.format(getString(R.string.create_wallet_fram_name),(listAccount.size+1)))
                } else {
                    edt_name.setText(String.format(getString(R.string.create_wallet_fram_name),"1"))
                }
            }
        }
    }


    fun TextView.onCreateWallte(): Unit {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val oldPsd1 = edt_name.text.toString().trim()
                val oldPs2 = edt_psd1.text.toString().trim()
                val sercert = edt_psd2.text.toString().trim()
                btn_create_wallet.isEnabled = (!TextUtils.isEmpty(oldPsd1)) && oldPs2.length >= Constant.PSDSIZE  && sercert.length>=Constant.PSDSIZE
            }
        })
    }



    private var isWebLoadFinsh = false

    private fun initWebView() {

        creat_webView.getSettings().setJavaScriptEnabled(true)
        val settings = creat_webView.getSettings()
        settings.setJavaScriptEnabled(true)
        settings.setDomStorageEnabled(true)
        settings.setAllowFileAccess(true)
        settings.setAppCacheEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        creat_webView.setWebViewClient(object : WebViewClient() {
            @Deprecated("")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
//                WebSSLDialog(view, handler, error)
            }

            override fun onPageFinished(view: WebView, url: String) {
                isWebLoadFinsh = true
                super.onPageFinished(view, url)
            }
        })
        creat_webView.loadUrl("file:////android_asset/crypto/test/index.html")
    }

    // "{"wallet":{"secret":"sha8CUNS5oNyvnVc6AhvR6hsWmAZv","address":"yH5rZtg93hgSeMTeBh4tTULV8ZERt47vuZ"},
    // "mnemonic":"TASK HUT JAR WIND BITE FATE LOOT NOR MAGI FAD NUT PAP"}"
    private fun creatGenerateAddress(psd1:String, nameWallet:String) {
        creat_webView.evaluateJavascript("javascript:generateAddress()",
            ValueCallback<String> { value ->
                try {
                    val gson = Gson()
                    val wallet = gson.fromJson<WalletBean>(value, WalletBean::class.java!!)
                    wallet?.let {
                        launch(Dispatchers.Main) {
                            wallet.psd=AesEBC.encrypt(psd1,psd1)
                            wallet.wallet.secret=AesEBC.encrypt(psd1, wallet.wallet.secret)
                            wallet.nameWallet=nameWallet

                            Log.e("Wallet",wallet.toString());

                            val intent = Intent(application, BackupsActivity::class.java)
                            intent.putExtra("data", wallet)
                            startActivity(intent)
                        }
                        return@ValueCallback
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                makeText(getString(R.string.js_error_info))
            })
    }


    fun onCreateWallte(view: View) {
        val nameWallet=edt_name.text.toString().trim()
        val psd1=edt_psd1.text.toString().trim()
        val psd2=edt_psd2.text.toString().trim()

         if(psd1!=psd2){
            makeText(getString(R.string.create_psd_nosaom))
            return
        }
        creatGenerateAddress(psd1,nameWallet)
    }


}