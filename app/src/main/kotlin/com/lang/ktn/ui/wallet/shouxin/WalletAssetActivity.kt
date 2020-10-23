package com.lang.ktn.ui.wallet.shouxin

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.SignBean
import com.lang.ktn.bean.WalletBean
import com.lang.ktn.bean.resp.CretCurr
import com.lang.ktn.bean.resp.HomeAsset
import com.lang.ktn.bean.resp.TraBeam
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.ui.manage.WalletKeyActivity
import com.lang.ktn.ui.showDialogSelPsd
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.ktn.utils.AesEBC
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_create_wallet.*
import kotlinx.android.synthetic.main.activity_magni_walletasset.*
import kotlinx.android.synthetic.main.activity_magni_walletasset.creat_webView
import kotlinx.android.synthetic.main.fragment_assets.*
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//授信
class WalletAssetActivity : BaseActivity() {
    var sqladdres: SqlAddres? = null
    var lines: ArrayList<HomeAsset>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magni_walletasset)
        sqladdres = intent.getParcelableExtra("data")
        lines = intent.getSerializableExtra("lines") as ArrayList<HomeAsset>?

        initWebView();
        nav_tittle.setText(getString(R.string.add_zichan_title))
        nav_img_left.setOnClickListener { finish() }

        val lay = LinearLayoutManager(this)
        lay.orientation = LinearLayoutManager.VERTICAL
        recycle_addset.layoutManager = lay
        tokenList();

    }

    // 获取资产和费率
    private fun accountInfo(cretcurr: CretCurr, secret: String) {
        sqladdres?.address?.let {
            retrofit<HomeAsset> {
                api = Api.instance.service.accountInfo(
                    mapOf(
                        Pair("Account", it),
                        Pair("withFee", "true")
                    )
                )
                onSuccess { bean: HomeAsset?, code: String, msg: String ->
                    bean?.let {

                        var valueShouxun = "0";
                        if (cretcurr.shouxin) {
                            valueShouxun = "0"
                        } else {
                            valueShouxun = cretcurr.value
                        }
                        var map = mapOf<String, Any?>(
                            Pair("Account", sqladdres?.address),
                            Pair("TransactionType", "TrustSet"),
                            Pair(
                                "LimitAmount",
                                mapOf(
                                    Pair("currency", cretcurr.currency),
                                    Pair("value", valueShouxun),
                                    Pair("issuer", cretcurr.issuer)
                                )
                            ),
                            Pair("Fee", bean.baseFee),
                            Pair("Flags", "131072"),
                            Pair("Sequence", bean.sequence)
                        )
                        sign(Gson().toJson(map), secret, cretcurr)
                    }
                }
                onFail { t: HomeAsset?, code: String, msg: String ->
                    if ("actNotFound" == code) {
                        makeText(getString(R.string.wallet_no_action))
                    } else {
                        makeText(msg)
                    }
                }
            }
        }
    }


    private fun sign(txJson: String, secret: String, cretcurr: CretCurr) {
        creat_webView.evaluateJavascript("javascript:sign('" + txJson + "','" + secret + "')",
            ValueCallback<String> { value ->
                try {
                    val gson = Gson()
                    val walletSignBean = gson.fromJson<SignBean>(value, SignBean::class.java!!)
                    walletSignBean?.let {
                        launch(Dispatchers.Main) {
                            transactionSubmit(walletSignBean, cretcurr)
                        }
                        return@ValueCallback
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                makeText(getString(R.string.js_error_info))
            })
    }

    private fun transactionSubmit(hash: SignBean, cretcurr: CretCurr) {
        retrofit<TraBeam> {
            api = Api.instance.service.transactionSubmit(hash)
            onSuccess { bean: TraBeam?, code: String, msg: String ->
                bean?.let {
                    if ("tesSUCCESS" == bean.engineResult) {
                        if (cretcurr.shouxin) {
                            makeText(getString(R.string.wallet_cancel_success))
                        } else {
                            makeText(getString(R.string.wallet_shouxi_success))
                        }

                        cretcurr.shouxin = !cretcurr.shouxin
                        recycle_addset.adapter?.notifyDataSetChanged()
                    } else {
                        makeText(bean.engineResultMessage)
                    }
                }
            }
            onFail { t: TraBeam?, code: String, msg: String ->
                makeText(msg)
            }
        }
    }

    private fun tokenList() {
        var dalogLoad = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2).setDimAmount(0.5f)
        dalogLoad.show()

        retrofit<ArrayList<CretCurr>> {
            api = Api.instance.service.tokenList()
            onSuccess { bean: ArrayList<CretCurr>?, code: String, msg: String ->
                dalogLoad.dismiss()
                bean?.let {
                    recycle_addset.adapter = MageAssetAadpter(bean, lines) { data ->
                        if (!TextUtils.isEmpty(data?.money)) {
                            var mo=data?.money.toFloat()
                            if(mo>0){
                             makeText(getString(R.string.walle_da_0_no))
                                return@MageAssetAadpter
                            }
                        }

                        showDialogSelPsd(getActivity()) { psd: String, dialog: Dialog, txt_infoView: TextView ->
                            var key: String? = null
                            try {
                                sqladdres?.key?.let {
                                    key = AesEBC.ecrypt(psd, it)
                                    key?.let {
                                        accountInfo(data, it)
                                    }
                                }
                            } catch (ex: java.lang.Exception) {
                                txt_infoView.setText(getString(R.string.dialog_psd_error))
                                return@showDialogSelPsd
                            }
                            dialog.dismiss()

                        }
                    }
                }
            }
            onFail { t: ArrayList<CretCurr>?, code: String, msg: String ->
                dalogLoad.dismiss()
                makeText(msg)
            }
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


}