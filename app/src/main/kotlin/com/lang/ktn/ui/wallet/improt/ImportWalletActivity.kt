package com.lang.ktn.ui.wallet.improt

import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.room.Room
import androidx.viewpager.widget.ViewPager
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.google.gson.Gson
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.TabEntity
import com.lang.ktn.bean.WalletGenerate
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.ui.main.MainActivity
import com.lang.ktn.ui.manage.WalletActivity
import com.lang.ktn.utils.AesEBC
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_import_walle.*
import kotlinx.android.synthetic.main.activity_import_walle.creat_webView
import kotlinx.android.synthetic.main.activity_reset_walletpsd.*
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.activity_wallet_backremember.*
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class ImportWalletActivity : BaseActivity() {

    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val listTitle = ArrayList<String>()

    private val mIconUnselectIds = intArrayOf(R.mipmap.ic_assets, R.mipmap.ic_backup)
    private val mIconUnselectIdsx = intArrayOf(R.mipmap.ic_assets, R.mipmap.ic_backup)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_walle)
        nav_tittle.setText(getString(R.string.import_wallet_title))
        nav_img_left.setOnClickListener{ finish() }

        listTitle.add(getString(R.string.import_wallet_zhujici))
        listTitle.add(getString(R.string.import_wallet_miyao))


        mTabEntities.add(TabEntity(getString(R.string.import_wallet_zhujici), mIconUnselectIds[0], mIconUnselectIds[0]))
        mTabEntities.add(TabEntity(getString(R.string.import_wallet_miyao), mIconUnselectIds[0], mIconUnselectIds[0]))

        val fragments = ArrayList<Fragment>()
        val emailLoginFragment = CardFragment()
        val phoneLoginFragment = ZxingFragment()

        fragments.add(phoneLoginFragment)
        fragments.add(emailLoginFragment)
        vp_login.setOffscreenPageLimit(2)


        val fAdapter = SpaceFragmentAdapter(supportFragmentManager, fragments, listTitle)
        vp_login.adapter = fAdapter
        tab_login.setTabData(mTabEntities)
        tab_login.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                vp_login.currentItem = position
            }

            override fun onTabReselect(position: Int) {}
        })
        vp_login.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                tab_login.currentTab = position
            }

        })
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


    fun secretAddress(oldPsd1:String,secret:String){
        creat_webView.evaluateJavascript("javascript:isValidAddress(\"" + secret + "\")",
            ValueCallback<String> { value ->
                try {
                    val gson = Gson()
                    val wallet = gson.fromJson<WalletGenerate>(value, WalletGenerate::class.java!!)
                    wallet?.let {
                        launch(Dispatchers.Main) {
                            onOkWallte(wallet, oldPsd1)
                        }
                        return@ValueCallback
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                makeText(getString(R.string.import_wallet_error_key))

            })
    }

    fun zhujici(oldPsd1:String,zhujici:String){
        creat_webView.evaluateJavascript("javascript:getSecretFromMaster(\"" + zhujici + "\")",
            ValueCallback<String> { value ->
                try {
                    val gson = Gson()
                    val wallet = gson.fromJson<WalletGenerate>(value, WalletGenerate::class.java!!)
                    wallet?.let {
                        launch(Dispatchers.Main) {
                            onOkWallte(wallet, oldPsd1)
                        }
                        return@ValueCallback
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                makeText(getString(R.string.import_wallet_error_zhujic))
            })
    }


    fun onOkWallte(wallet: WalletGenerate,psd1:String) {
        launch(Dispatchers.IO) {
            var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
            var haveWallet = accountTable.accountDao.getUserById(wallet.address)
            withContext(Dispatchers.Main) {
                if (haveWallet != null && !TextUtils.isEmpty(haveWallet.address)) {
                    makeText(getString(R.string.import_wallet_haveq))
                    return@withContext
                }
                launch(Dispatchers.IO) {
                    var listAccount = accountTable.accountDao.queryAllUserInfo()
                    listAccount?.let {
                        for (index in 0..listAccount.size - 1) {
                            val cancalDault = listAccount.get(index)
                            cancalDault.afault = "0"
                            accountTable.accountDao.updateUser(cancalDault)
                        }
                    }
                    val addres = SqlAddres()
                    addres.address = wallet?.address;
                    addres.key = AesEBC.encrypt(psd1, wallet.secret)
                    addres.nickName =String.format(getString(R.string.create_wallet_fram_name),(listAccount.size + 1))
                    addres.afault = "1"
                    var insert = accountTable.accountDao.insertUser(addres)
                    withContext(Dispatchers.Main) {
                        val intent = Intent(application, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            }

        }


    }


}