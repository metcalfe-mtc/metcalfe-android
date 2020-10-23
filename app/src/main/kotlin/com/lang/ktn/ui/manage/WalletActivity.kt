package com.lang.ktn.ui.manage

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
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
import com.lang.ktn.ui.showDialogSelPsd
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.ktn.ui.wallet.ModefyResetWalltePsdActivity
import com.lang.ktn.ui.wallet.ModefyWalltePsdActivity
import com.lang.ktn.ui.wallet.ModifyNameActivity
import com.lang.ktn.utils.AesEBC
import com.lang.ktn.utils.QrUtils
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_assets_zhuancuh.*
import kotlinx.android.synthetic.main.activity_create_wallet.*
import kotlinx.android.synthetic.main.activity_reset_walletpsd.*
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.activity_wallet.creat_webView
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception

class WalletActivity : BaseActivity() {

    var sqladdres: SqlAddres? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        nav_img_left.setOnClickListener { finish() }

        sqladdres = intent.getParcelableExtra("data")
        nav_tittle.setText(sqladdres?.nickName)
        sqladdres?.let {
            txt_w_d_name.setText(it.nickName)
            txt_w_d_addres.setText(it.address)

            if (it.afault != "1") {
                layout_delet.visibility = View.VISIBLE
            }

        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        layout_delet.setOnClickListener {
            showDialogSelPsd(getActivity()) { psd: String, dialog: Dialog, txt_infoView: TextView ->
                var key: String? = null
                try {
                    sqladdres?.key?.let {
                        key = AesEBC.ecrypt(psd, it)

                    }
                } catch (ex: Exception) {
                    txt_infoView.setText(getString(R.string.dialog_psd_error))
                    return@showDialogSelPsd
                }
                dialog.dismiss()
                key?.let { it1 ->
                    launch(Dispatchers.IO) {
                        var accountTable = Room.databaseBuilder(
                            application,
                            AbstractUserDataBase::class.java,
                            "userDataBase"
                        ).build()
                        sqladdres?.let {
                            accountTable.accountDao.deleteUser(it)
                            withContext(Dispatchers.Main) {
                                makeText(getString(R.string.delete_walllet_dc))
                                EventBus.getDefault().post("event2")
                                finish()
                            }
                        }
                    }

                };
            }
        }

        init()
        initWebView();
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

    private fun initData() {
        sqladdres?.let {
            txt_w_d_name.setText(it.nickName)
            txt_w_d_addres.setText(it.address)
        }

        launch(Dispatchers.IO) {
            var accountTable =
                Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase")
                    .build()
            sqladdres?.id?.let {
                sqladdres = accountTable.accountDao.getUserById(it)
                sqladdres?.let {
                    withContext(Dispatchers.Main) {
                        sqladdres?.let {
                            txt_w_d_name.setText(it.nickName)
                            txt_w_d_addres.setText(it.address)
                        }
                    }
                }
            }


        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String?) {
        if ("event3" == event) {
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);
    }

    val REQUESTCODE = 190;
    private fun init() {
        layout_mody_name.setOnClickListener {
            val intent = Intent(this, ModifyNameActivity::class.java)
            intent.putExtra("data", sqladdres)
            startActivity(intent)
        }
        layout_addres.setOnClickListener {
            val intent = Intent(this, WalletAddressActivity::class.java)
            intent.putExtra("data", sqladdres)
            startActivity(intent)
        }
        layout_key.setOnClickListener {
            launch(Dispatchers.IO) {
                var accountTable = Room.databaseBuilder(
                    application,
                    AbstractUserDataBase::class.java,
                    "userDataBase"
                ).build()
                sqladdres?.id?.let {
                    sqladdres = accountTable.accountDao.getUserById(it)
                    sqladdres?.let {
                        withContext(Dispatchers.Main) {
                            showDialogSelPsd(getActivity()) { psd: String, dialog: Dialog, txt_infoView: TextView ->
                                var key: String? = null
                                try {
                                    sqladdres?.key?.let {
                                        key = AesEBC.ecrypt(psd, it)
                                    }
                                } catch (ex: Exception) {
                                    txt_infoView.setText(getString(R.string.dialog_psd_error))
                                    return@showDialogSelPsd
                                }
                                dialog.dismiss()
                                sqladdres?.secret = key;
                                val intent = Intent(application, WalletKeyActivity::class.java)
                                intent.putExtra("data", sqladdres)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
        layout_mody_psd.setOnClickListener {
            val intent = Intent(this, ModefyWalltePsdActivity::class.java)
            intent.putExtra("data", sqladdres)
            startActivityForResult(intent, REQUESTCODE)
        }
        layout_forget_psd.setOnClickListener {
            val intent = Intent(this, ModefyResetWalltePsdActivity::class.java)
            intent.putExtra("data", sqladdres)
            startActivity(intent)
        }
        layout_daochu_zhujici.setOnClickListener {
            checkPaswwad()
//            val intent = Intent(this, BackupsActivity::class.java)
//            intent.putExtra("dataZhuji", sqladdres)
//            startActivity(intent)
        }

    }


    fun checkPaswwad(): Unit {
        launch(Dispatchers.IO) {
            var accountTable = Room.databaseBuilder(
                application,
                AbstractUserDataBase::class.java,
                "userDataBase"
            ).build()
            sqladdres?.id?.let {
                sqladdres = accountTable.accountDao.getUserById(it)
                sqladdres?.let {
                    withContext(Dispatchers.Main) {
                        showDialogSelPsd(getActivity()) { psd: String, dialog: Dialog, txt_infoView: TextView ->
                            var key: String? = null
                            try {
                                sqladdres?.key?.let {
                                    key = AesEBC.ecrypt(psd, it)
                                }
                            } catch (ex: Exception) {
                                txt_infoView.setText(getString(R.string.dialog_psd_error))
                                return@showDialogSelPsd
                            }
                            dialog.dismiss()
                            key?.let { it1 -> finZhujici(it1) };
                        }
                    }
                }
            }
        }
    }

    fun finZhujici(secret: String) {
        creat_webView.evaluateJavascript("javascript:getMasterFromKey(\"" + secret + "\")",
            ValueCallback<String> { value ->
                try {
                    val gson = Gson()
                    val wallet = gson.fromJson<WalletBean>(value, WalletBean::class.java!!)
                    wallet?.let {
                        launch(Dispatchers.Main) {
                            wallet.psd = "huxiaoshit"
                            wallet.wallet.secret = wallet.wallet.secret
                            wallet.nameWallet = "Wallet"

                            val intent = Intent(application, BackupsActivity::class.java)
                            intent.putExtra("data", wallet)
                            intent.putExtra("zhujici", true)
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


}