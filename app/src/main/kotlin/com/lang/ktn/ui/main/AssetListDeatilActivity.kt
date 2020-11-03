package com.lang.ktn.ui.main

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.resp.CretCurr
import com.lang.ktn.bean.resp.HashTx
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.ui.showDialogSelPsd
import com.lang.ktn.ui.wallet.shouxin.MageAssetAadpter
import com.lang.ktn.utils.AesEBC
import com.lang.ktn.utils.Constant
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_assets_recode.*
import kotlinx.android.synthetic.main.activity_magni_walletasset.*
import kotlinx.android.synthetic.main.activity_wallet_detail.*
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.w3c.dom.Text
import java.lang.Exception
import java.nio.file.DirectoryStream
import java.text.SimpleDateFormat
import java.util.*

class AssetListDeatilActivity: BaseActivity() {
    var hash:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_detail)

        nav_tittle.setText(getString(R.string.zichan_detail_title))
        nav_img_left.setOnClickListener{ finish() }

        hash=intent.getStringExtra("hash")
        initClick()
        tokenList()
    }

    private fun initClick() {
        imag_shouKuan_addres.setOnClickListener {
            var sKr=txt_shouKuan_addres.text.toString().trim()
            val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText(sKr, sKr)
            cm.setPrimaryClip(mClipData)
            makeText(getString(R.string.zichan_detail_copy_success))
        }
        img_fuKuan_addres.setOnClickListener {
            var sKr=txt_fuKuan_addres.text.toString().trim()
            val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText(sKr, sKr)
            cm.setPrimaryClip(mClipData)
            makeText(getString(R.string.zichan_detail_copy_success))
        }
        img_hash.setOnClickListener {
            var sKr=txt_hash.text.toString().trim()
            val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText(sKr, sKr)
            cm.setPrimaryClip(mClipData)
            makeText(getString(R.string.zichan_detail_copy_success))
        }
    }

    fun getTextName(date:Long):String{
        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(Date(date))
    }

    private fun tokenList() {
        retrofit<HashTx> {
            api = Api.instance.service.transactionTx(hash!!)
            onSuccess{ bean: HashTx?, code: String, msg: String->
                bean?.let {


                    if ("TrustSet"==it.transactionType){
                        tv_fukuanfang.setText(getString(R.string.zichan_detail_faxingfang))
                        tv_shoukuanfang.setText(getString(R.string.zichan_detail_faxingshang))
                        txt_shouKuan_addres.setText(it.limitAmount.issuer)
                        txt_fuKuan_addres.setText(it.account )
                        tv_amount_currency.setText(getString(R.string.zichan_detail_currency))
                        if (TextUtils.isEmpty(it.limitAmount?.currency)) {
                            txt_jine.setText("M")
                        } else {
                            txt_jine.setText(it.limitAmount?.currency)
                        }
                    }else {
                        tv_amount_currency.setText(getString(R.string.zichan_detail_jine))
                        tv_fukuanfang.setText(getString(R.string.zichan_detail_fukuanafng))
                        tv_shoukuanfang.setText(getString(R.string.zichan_detail_shoukuanfang))
                        txt_shouKuan_addres.setText(it.destination)
                        txt_fuKuan_addres.setText(it.account )
                        if (TextUtils.isEmpty(it.amount?.currency)) {

                            var allMoeny =
                                it.amount?.value?.toLong()!!
                                    .div(Math.pow(10.0, it.amount.decimals.toDouble()))
                                    .toString()
                            txt_jine.setText(
                                allMoeny.toBigDecimal().stripTrailingZeros()
                                    .toPlainString() + " " + Constant.CURRN
                            )
                        } else {
                            txt_jine.setText(
                                it.amount?.value?.toBigDecimal().stripTrailingZeros()
                                    .toPlainString() + " " + it.amount?.currency
                            )
                        }
                    }

                    var fee = it.fee?.toLong().div(Math.pow(10.0, 6.0)).toString()
                    txt_fee.setText(fee.toBigDecimal().stripTrailingZeros().toPlainString() +" "+ Constant.CURRN)
                    txt_type.setText(it.transactionType)
                    txt_statue.setText(it.transactionResult)
                    txt_hash.setText(it.hash)

                    if(it.validated){
                        txt_statue_p.visibility=View.GONE
                        txt_statue_b.visibility=View.GONE
                        txt_statue_t.visibility=View.GONE

                        if("tesSUCCESS"==it.transactionResult){
                            txt_statue.setText(getString(R.string.zichan_detail_success_staue))
                            txt_block.setText(it.ledgerIndex.toString())
                            txt_time.setText(getTextName(it.date))
                        }else{
                            txt_statue.setText(getString(R.string.zichan_list_fail))
                            txt_block.setText(it.ledgerIndex.toString())
                            txt_time.setText(getTextName(it.date))
                        }

                        it.memos?.let {
                            if(it.size>0 && !TextUtils.isEmpty(it[0].memoDataText)){
                                layout_beizhu.visibility= View.VISIBLE
                                txt_beizhu.setText(it[0].memoDataText)
                            }
                        }

                    }else{
                        launch (Dispatchers.IO){
                            delay(1000L)
                            tokenList()
                        }
                    }

//                    if("tesSUCCESS"==it.transactionResult){
//                        txt_statue_p.visibility=View.GONE
//                        txt_statue_b.visibility=View.GONE
//                        txt_statue_t.visibility=View.GONE
//
//                        txt_statue.setText(getString(R.string.zichan_detail_success_staue))
//                        txt_block.setText(it.ledgerIndex.toString())
//                        txt_time.setText(getTextName(it.date))
//
//
//                    }else{
////                        txt_statue.setText("Panding")
//                        launch (Dispatchers.IO){
//                            delay(1000L)
//                            tokenList()
//                        }
//                    }
//                    it.memos?.let {
//                        if(it.size>0 && !TextUtils.isEmpty(it[0].memoDataText)){
//                            layout_beizhu.visibility= View.VISIBLE
//                            txt_beizhu.setText(it[0].memoDataText)
//                        }
//                    }

                }
            }
            onFail{ t: HashTx?, code: String, msg: String->
                launch (Dispatchers.IO){
                    delay(1000L)
                    tokenList()
                }
            }
        }
    }


}