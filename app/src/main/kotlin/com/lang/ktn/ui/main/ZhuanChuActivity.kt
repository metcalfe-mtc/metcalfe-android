package com.lang.ktn.ui.main

import android.Manifest
import android.app.Dialog
import android.app.PendingIntent.getActivity
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
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.SignBean
import com.lang.ktn.bean.SignVailBean
import com.lang.ktn.bean.resp.AccountLines
import com.lang.ktn.bean.resp.CretCurr
import com.lang.ktn.bean.resp.HomeAsset
import com.lang.ktn.bean.resp.TraBeam
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.ui.showDialogSelPsd
import com.lang.ktn.utils.AesEBC
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.QrUtils
import kotlinx.android.synthetic.main.activity_assets_zhuancuh.*
import kotlinx.android.synthetic.main.activity_assets_zhuancuh.creat_webView
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AfterPermissionGranted
import java.lang.Exception
import java.math.BigDecimal
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.kaopiz.kprogresshud.KProgressHUD
import com.lang.ktn.utils.NumInputFilter
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_assets_recode.*
import kotlinx.android.synthetic.main.activity_assets_zhuancuh.btn_zhuanchu
import kotlinx.android.synthetic.main.fragment_zhujici.*
import java.io.UnsupportedEncodingException
import kotlin.experimental.and


class ZhuanChuActivity : BaseActivity() {

    public val RC_CAMERA_PERM = 123
    public val RC_LOCATION_CONTACTS_PERM = 124

    var currency: String? = null;
    var homeasset: HomeAsset? = null;
    var mFee: String? = null
    var mMoney: String? = null
    var Sequence: Long? = null
    var sqladdres: SqlAddres?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.lang.progrom.R.layout.activity_assets_zhuancuh)
        currency = intent.getStringExtra("currency")
        homeasset = intent.getParcelableExtra("data")
        sqladdres=intent.getParcelableExtra("sqladdres")
        nav_tittle.setText(homeasset?.currency)
//        nav_tittle.setText(getString(R.string.asset_bi_zhuabi) + homeasset?.currency)
        nav_img_left.setOnClickListener{ finish() }

        initWebView()
        btn_zhuanchu.setOnClickListener {
            submitZhuang()
        }
        layout_scan.setOnClickListener {
            methodRequiresTwoPermission()
        }
        edt_moeny_input.setFilters(arrayOf<InputFilter>(NumInputFilter(16, 6)))
        allTx_money.setOnClickListener {
           if(!TextUtils.isEmpty(mMoney)){
               edt_moeny_input.setText(mMoney.toString())
           }
        }
        homeasset?.currency?.let {
            if (Constant.CURRN == it) {
                accountInfo(
                    mapOf(Pair("account", currency!!), Pair("withFee", "true")),
                    true
                );
            } else {
                accountInfo(
                    mapOf(Pair("account",currency!!), Pair("withFee", "true")),
                    false
                );
                accountList(mapOf(Pair("account", currency!!)))
            }
        }
        edt_txt_addres.onCreateWallte()
        edt_moeny_input.onCreateWallte()
    }



    fun TextView.onCreateWallte(): Unit {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val oldPsd1 = edt_txt_addres.text.toString().trim()
                val oldPs2 = edt_moeny_input.text.toString().trim()

                btn_zhuanchu.isEnabled = oldPsd1.length >= Constant.PSDSIZE  && oldPs2.isNotEmpty()
            }
        })
    }




    @Throws(UnsupportedEncodingException::class)
    fun hexadecimal(input: String?, charsetName: String): String {
        if (input == null) throw NullPointerException()
        return asHex(input.toByteArray(charset(charsetName))).toUpperCase() // toLowerCase()
    }
    private val HEX_CHARS = "0123456789abcdef".toCharArray()
    fun asHex(buf: ByteArray): String {
        val chars = CharArray(2 * buf.size)
        for (i in buf.indices) {
            chars[2 * i] = HEX_CHARS[(buf[i].toInt() and 0xF0).ushr(4)];
            chars[2 * i + 1] = HEX_CHARS[(buf[i] and  0x0F).toInt()];
        }
        return String(chars)
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

    private fun submitZhuang() {
        if (mMoney == null) {
            makeText(getString(R.string.asset_bi_please))
            return
        }
        var addres = edt_txt_addres.text.toString().trim()
        var money = edt_moeny_input.text.toString().trim()
        var motor = txt_motor.text.toString().trim()
        try {
            val mAll = mMoney?.toFloat()
            val mInput = money.toFloat()
            if (mAll != null && mAll < mInput) {
                makeText(getString(R.string.asset_bi_zichanbuzu))
                return
            }
            var mmap = mutableMapOf<String, Any>()
            mmap["Account"] = currency!!
            mmap["Destination"] = addres
            mmap["TransactionType"] = "Payment"
            if (!TextUtils.isEmpty(motor)) {
                val map1 = mapOf(Pair("MemoType", "54455854"), Pair("MemoData", hexadecimal(motor,"UTF-8")))
                val map = mapOf(Pair("Memo", map1))
                var memos = arrayListOf(map);
                mmap["Memos"] = memos
            }
            homeasset?.currency?.let {
                if (Constant.CURRN == it) {
                    mmap["Amount"] = money.toBigDecimal().multiply((Math.pow(10.0, 6.0)).toBigDecimal()).stripTrailingZeros().toPlainString()
                } else {
                    val map1 = mapOf(
                        Pair("currency", it),
                        Pair("value", money),
                        Pair("issuer", homeasset!!.account)
                    )
                    mmap["Amount"] = map1
                }
            }
            if (mFee == null) {
                mmap["Fee"] = "100"
            } else {
                mmap["Fee"] = mFee!!

            }
            mmap["Flags"] = "2147483648"
            mmap["Sequence"] = (Sequence!!)


            showDialogSelPsd(getActivity()) { psd: String, dialog: Dialog, txt_infoView: TextView ->
                var key:String?=null
                try {
                    sqladdres?.key?.let {
                        key= AesEBC.ecrypt(psd, it)
                        key?.let {
                            sign(Gson().toJson(mmap),it,addres)
                        }
                    }
                }catch (ex: Exception){
                    txt_infoView.setText(getString(R.string.dialog_psd_error))
                    return@showDialogSelPsd
                }
                dialog.dismiss()

            }

        } catch (E: Exception) {
            E.message?.let { makeText(it) }
        }
    }





    private fun sign(txJson: String, secret: String,destination:String) {
        var dalogX = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2).setDimAmount(0.5f)
        dalogX.show()


        creat_webView.evaluateJavascript(
            "javascript:signWithAddres('$txJson','$secret','$destination')",
            ValueCallback<String> { value ->
                try {
                    val gson = Gson()
                    val walletSignBean = gson.fromJson<SignVailBean>(value, SignVailBean::class.java!!)
                    walletSignBean?.let {
                        launch(Dispatchers.Main) {
                            if(walletSignBean.valid==null || walletSignBean.valid==false){
                                dalogX.dismiss()
                                makeText(getString(R.string.zhuanchu_addres_eorr))
                            }else {
                                transactionSubmit(walletSignBean.sign!!,dalogX)
                            }
                        }
                        return@ValueCallback
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                makeText(getString(R.string.js_error_info))
            })
    }

    private fun transactionSubmit(hash: SignBean,dialogX:KProgressHUD) {
        retrofit<TraBeam> {
            api = Api.instance.service.transactionSubmit(hash)
            onSuccess { bean: TraBeam?, code: String, msg: String ->
                bean?.let {
                    dialogX.dismiss()
                    if ("tesSUCCESS" == bean.engineResult) {
                        val intent = Intent(getActivity(), AssetListDeatilActivity::class.java)
                        intent.putExtra("hash",it.hash)
                        startActivity(intent)
                        finish()
                    } else {
                        makeText(bean.engineResultMessage)
                    }
                }
            }
            onFail { t: TraBeam?, code: String, msg: String ->
                dialogX.dismiss()
                makeText(msg)
            }
        }

    }


    private fun accountInfo(map: Map<String, String>, isCurren: Boolean) {
        retrofit<HomeAsset> {
            api = Api.instance.service.accountInfo(map)
            onSuccess { bean: HomeAsset?, code: String, msg: String ->
                bean?.let {
                    try {
//                        val doMoney = bean.reserveBase!!.toLong()
//                            .and(bean.reserveInc!!.toLong().minus(bean.ownerCount!!.toLong()))
//                            .div(Math.pow(10.0, it.decimals!!.toDouble()))
//                        var allMoeny =
//                            bean.balance?.toLong()!!.div(Math.pow(10.0, it.decimals!!.toDouble()))
//                                .toString()
                        var fee = bean.baseFee?.toLong()!!.div(Math.pow(10.0, it.decimals!!.toDouble())).toString()

                        mFee = it.baseFee
                        Sequence = it.sequence
                        txt_fee.setText(String.format(getString(R.string.zhuanchu_fee), BigDecimal(fee).stripTrailingZeros().toPlainString()))
                        if (isCurren) {
                            val dongjie= getFatckMoney(it.reserveBase!!,it.reserveInc!!,it.ownerCount!!.toString())
                            val doMoney=getBalanceMoney(it.balance!!)
                            mMoney=doMoney.subtract(dongjie).stripTrailingZeros().toPlainString()

                            edt_moeny_input.setFilters(arrayOf<InputFilter>(NumInputFilter(16,
                                it.decimals!!
                            )))

                            edt_moeny_input.setHint(String.format(getString(R.string.available_1000_sde),mMoney.toString(), homeasset?.currency))
                        }
                    } catch (E: Exception) {
                    }
                }
            }
            onFail { t: HomeAsset?, code: String, msg: String ->

            }
        }
    }


    fun getBalanceMoney(balance:String): BigDecimal {
        val b1= java.math.BigDecimal(balance)
        return b1.divide(BigDecimal(Math.pow(10.0,Constant.DECIMALS.toDouble()).toString()))
    }


    fun getFatckMoney(reserveBase:String,reserveInc:String,ownerCount:String): BigDecimal {
        val b1= java.math.BigDecimal(reserveBase)
        val b2=java.math. BigDecimal(reserveInc)
        val b3= java.math.BigDecimal(ownerCount)
        return b1.add(b2.multiply(b3)).divide(BigDecimal(Math.pow(10.0,Constant.DECIMALS.toDouble()).toString()))
    }



    private fun accountList(map: Map<String, String>) {
        retrofit<AccountLines> {
            api = Api.instance.service.accountLines(map)
            onSuccess { bean: AccountLines?, code: String, msg: String ->
                bean?.lines?.let {
                    for (index in it.indices) {
                        var b1 = it[index];
                        if (homeasset!!.currency == b1.currency) {
                            val xa = b1.balance!!.toDouble();
                            edt_moeny_input.setFilters(arrayOf<InputFilter>(NumInputFilter(16,
                                b1.decimalsLimit!!
                            )))

                            if (xa < 0) {
                                mMoney = "0"
//                                edt_moeny_input.setHint("0")
                                edt_moeny_input.setHint(String.format(getString(R.string.available_1000_sde),"0", homeasset?.currency))
                            } else {
//                                txt_vail_money.setText(b1.balance)
                                mMoney = b1.balance
                                edt_moeny_input.setHint(String.format(getString(R.string.available_1000_sde),mMoney.toString(), homeasset?.currency))
//                                edt_moeny_input.setHint(b1.balance)
                            }
                        }
                    }
                }
            }
            onFail { t: AccountLines?, code: String, msg: String ->

            }
        }
    }


    @AfterPermissionGranted(value = 123)
    fun methodRequiresTwoPermission() {
        val perms = arrayOf<String>(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            QrUtils.getInstance(this).startScan(this) { edt_txt_addres.setText(it) }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.import_wallet_carmar_p), RC_CAMERA_PERM, *perms)
        }
    }

}