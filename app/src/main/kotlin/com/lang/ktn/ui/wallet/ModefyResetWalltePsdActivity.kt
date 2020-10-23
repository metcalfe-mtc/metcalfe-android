package com.lang.ktn.ui.wallet

import android.Manifest
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.room.Room
import com.google.gson.Gson
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.WalletBean
import com.lang.ktn.bean.WalletGenerate
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.ktn.utils.AesEBC
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.QrUtils
import com.lang.ktn.utils.changeEyes
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_reset_walletpsd.*
import kotlinx.android.synthetic.main.activity_reset_walletpsd.creat_webView
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class ModefyResetWalltePsdActivity : BaseActivity() {
    var sqladdres: SqlAddres? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_walletpsd)
        sqladdres = intent.getParcelableExtra("data")
        nav_tittle.setText(getString(R.string.reset_jy_psd))
        nav_img_left.setOnClickListener{ finish() }

        initWebView();

        edt_old_psd.onCreateWallte();
        edt_newpsd_1.onCreateWallte();
        edt_newpsd_2.onCreateWallte();


        lookPwd_create11.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_1,isChecked) }
        lookPwd_create12.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_2,isChecked) }

        layout_cli_zxing.setOnClickListener {
            methodRequiresTwoPermission()
        }
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

    fun onCreateWallte(view: View) {
        val edt_old_Sercit = edt_old_psd.text.toString().trim();

        val oldPsd1 = edt_newpsd_1.text.toString().trim()
        val oldPs2 = edt_newpsd_2.text.toString().trim()
        if (oldPsd1 != oldPs2) {
            makeText(getString(R.string.reset_jy_psd_nosome))
            return
        }

        creat_webView.evaluateJavascript("javascript:isValidAddress(\"" + edt_old_Sercit + "\")",
            ValueCallback<String> { value ->
                try {
                    val gson = Gson()
                    val wallet = gson.fromJson<WalletGenerate>(value, WalletGenerate::class.java!!)
                    wallet?.let {
                        launch(Dispatchers.Main) {
                            goneCheakWallet(wallet, oldPsd1)
                        }
                        return@ValueCallback
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                makeText(getString(R.string.import_wallet_error_key))
            })
    }

    fun goneCheakWallet(wallet: WalletGenerate, psd: String): Unit {

        if (sqladdres?.address == wallet.address) {
            launch(Dispatchers.IO) {
                var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
                try {
                    var key = AesEBC.encrypt(psd, wallet.secret)
                    sqladdres?.key = key
                    accountTable.accountDao.updateUser(sqladdres!!)
                } catch (ex: java.lang.Exception) {
                    makeText(getString(R.string.dialog_psd_error))
                    return@launch
                }
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        } else {
            makeText(getString(R.string.reset_psd_key_ero))
        }


    }


    fun TextView.onCreateWallte(): Unit {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val oldPsd = edt_old_psd.text.toString().trim()
                val oldPsd1 = edt_newpsd_1.text.toString().trim()
                val oldPs2 = edt_newpsd_2.text.toString().trim()
                btn_mdy_pk.isEnabled =
                    oldPsd.length >= Constant.PSDSIZE  && oldPsd1.length >= Constant.PSDSIZE  && oldPs2.length >= Constant.PSDSIZE
            }
        })
    }


    val RC_CAMERA_PERM = 123
    @AfterPermissionGranted(value = 123)
    fun methodRequiresTwoPermission() {
        val perms = arrayOf<String>(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            QrUtils.getInstance(this).startScan(this) { edt_old_psd.setText(it) }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.import_wallet_carmar_p), RC_CAMERA_PERM, *perms)
        }
    }


}