package com.lang.ktn.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.resp.AccountLines
import com.lang.ktn.bean.resp.HashTx
import com.lang.ktn.bean.resp.HomeAsset
import com.lang.ktn.bean.resp.Transactions
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.ui.wallet.shouxin.MageAssetAadpter
import com.lang.ktn.utils.Constant
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_assets_recode.*
import kotlinx.android.synthetic.main.activity_magni_walletasset.*
import kotlinx.android.synthetic.main.fragment_assets.*
import kotlinx.android.synthetic.main.nav_layout.*
import java.lang.Exception
import java.math.BigDecimal

class AssetListActivity : BaseActivity() {

    var homeasset: HomeAsset? = null;
    var currency: String? = null;
    var sqladdres: SqlAddres? = null

    var transactions: MutableList<HashTx>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assets_recode)
        nav_tittle.setText(getString(R.string.asset_bi_title))
        nav_img_left.setOnClickListener { finish() }

        homeasset = intent.getParcelableExtra("data")
        currency = intent.getStringExtra("currency")
        sqladdres = intent.getParcelableExtra("sqladdres")
        nav_tittle.setText(homeasset?.currency)
        homeasset?.run {
            account?.let {
                if ("M" != currency) {
                    issuer = it;
                }
            }
        }

        val lay = androidx.recyclerview.widget.LinearLayoutManager(this)
        lay.orientation = LinearLayoutManager.VERTICAL
        recycle_list_info.layoutManager = lay
        transactions = mutableListOf<HashTx>()
        recycle_list_info.adapter = AssetListAadpter(currency!!, transactions!!) {
            val intent = Intent(this, AssetListDeatilActivity::class.java)
            intent.putExtra("hash", it.hash)
            startActivity(intent)
        }
        btn_zhuanchu.setOnClickListener {
            var infoMeny = txt_vail_money.text.toString().trim();
            try {
                var money = infoMeny.toDouble()
                if (money <= 0) {
                    makeText(getString(R.string.asset_bi_zichanbuzu))
                    return@setOnClickListener
                }
            } catch (E: Exception) {
                makeText(getString(R.string.asset_bi_pleate_wiht))
                return@setOnClickListener
            }
            val intent = Intent(this, ZhuanChuActivity::class.java)
            intent.putExtra("data", homeasset)
            intent.putExtra("currency", currency)
            intent.putExtra("sqladdres", sqladdres)
            startActivity(intent)
        }
        homeasset?.currency?.let {
            nav_tittle.setText(it)
            if (Constant.CURRN == it) {
                accountInfo(mapOf(Pair("account", homeasset!!.account!!), Pair("withFee", "true")));
                txt_dongjie.visibility = View.VISIBLE

                homeasset?.let {
                    var dongjie =
                        getFatckMoney(it.reserveBase!!, it.reserveInc!!, it.ownerCount!!.toString())
                    val doMoney = getBalanceMoney(it.balance!!)
                    if (doMoney.compareTo(dongjie) == -1) {
                        dongjie = doMoney
                    }
                    txt_dongjie.setText(getString(R.string.asset_bi_dongjie) + dongjie)

                    txt_vail_money.setText(getVailMoney(doMoney, dongjie))
                }

            } else {
                accountList(mapOf(Pair("account", currency!!)))
                txt_dongjie.visibility = View.INVISIBLE
                txt_vail_money.setText(
                    homeasset?.balance?.toBigDecimal()?.stripTrailingZeros()?.toPlainString()
                )
            }
        }
        transactionAccountHistoryList()

        refreshLayout_aas.setOnRefreshListener {
            mMarker = ""
            transactions?.clear()
            recycle_list_info.adapter?.notifyDataSetChanged()
            layout_data.visibility = View.GONE
            transactionAccountHistoryList()
            homeasset?.currency?.let {
                nav_tittle.setText(it)
                if (Constant.CURRN == it) {
                    accountInfo(
                        mapOf(
                            Pair("account", homeasset!!.account!!),
                            Pair("withFee", "true")
                        )
                    );
                } else {
                    accountList(mapOf(Pair("account", currency!!)))
                }
            }

        }

        refreshLayout_aas.setOnLoadMoreListener {
            transactionAccountHistoryList();
        }

    }

    private fun getVailMoney(doMoney: BigDecimal, dongjie: BigDecimal): String {
        var vailMoney = doMoney.subtract(dongjie)?.stripTrailingZeros()?.toPlainString()
        if (doMoney?.compareTo(dongjie) == -1) {
            vailMoney = BigDecimal("0").stripTrailingZeros()?.toPlainString()
        }
        if (vailMoney?.compareTo("0") == -1) {
            vailMoney = BigDecimal("0").stripTrailingZeros()?.toPlainString()
        }
        return vailMoney.toString().trim();
    }


    var transactionsTemp: List<HashTx>? = null  //当前请求批号的所有数据
    var mTempIndex = 0
    var mMarker = ""
    var issuer = ""

    private fun transactionAccountHistoryList() {
        var mapmu = mutableMapOf<String, String>()
        mapmu["account"] = currency!!
        mapmu["limit"] = "10"
        if (!TextUtils.isEmpty(mMarker)) {
            mapmu["marker"] = mMarker
        }
        if (!TextUtils.isEmpty(issuer)) {
            mapmu["issuer"] = issuer
        }
        retrofit<Transactions> {
            api = Api.instance.service.paymentHistoryList(mapmu)
            onSuccess { bean: Transactions?, code: String, msg: String ->

                if (refreshLayout_aas != null) {
                    refreshLayout_aas.finishRefresh();
                }
                bean?.let {
                    try {
                        if (TextUtils.isEmpty(bean.marker)) {
                            mMarker = ""
                            refreshLayout_aas.setEnableLoadMore(false)
                        } else {
                            mMarker = bean.marker
                        }


                        if (bean.transactions != null) {
                            transactions!!.addAll(bean.transactions)
                            recycle_list_info.adapter?.notifyDataSetChanged()
                            transactions?.let {
                                if (it.size > 0) {
                                    layout_data.visibility = View.VISIBLE
                                }
                            }
                        }
                    } catch (E: Exception) {
                        E.printStackTrace()
                    }
                }
            }
            onFail { t: Transactions?, code: String, msg: String ->
                if (refreshLayout_aas != null) {
                    refreshLayout_aas.finishRefresh();
                }
            }
        }

    }


    private fun accountInfo(map: Map<String, String>) {
        retrofit<HomeAsset> {
            api = Api.instance.service.accountInfo(map)
            onSuccess { bean: HomeAsset?, code: String, msg: String ->
                bean?.let {
                    try {
//                        val doMoney=bean.reserveBase!!.toLong().and(bean.reserveInc!!.toLong().minus(bean.ownerCount!!.toLong())).div(Math.pow(10.0,Constant.DECIMALS.toDouble()))
//                        var allMoeny=bean.balance?.toLong ()!!.div(Math.pow(10.0,it.decimals!!.toDouble())).toString()
//                        var factMoeny=allMoeny.toDouble()-doMoney.toDouble();
//                        val doMoney=homeasset?.reserveBase!!.toLong().plus(homeasset?.reserveInc!!.toLong().times(homeasset?.ownerCount!!.toLong())).div(Math.pow(10.0,Constant.DECIMALS.toDouble()))
//                        var allMoeny=homeasset?.balance?.toDouble()
//                        var factMoeny= allMoeny?.minus(doMoney);

                        bean?.let {
                            var dongjie = getFatckMoney(
                                it.reserveBase!!,
                                it.reserveInc!!,
                                it.ownerCount!!.toString()
                            )
                            val doMoney = getBalanceMoney(it.balance!!)
                            if (doMoney.compareTo(dongjie) == -1) {
                                dongjie = doMoney
                            }
                            txt_dongjie.setText(getString(R.string.asset_bi_dongjie) + dongjie)
//                            doMoney.subtract(dongjie)?.stripTrailingZeros()?.toPlainString()
                            txt_vail_money.setText(getVailMoney(doMoney, dongjie))
                        }

//                        homeasset!!.balance=factMoeny.toString();
//                        txt_dongjie.setText(getString(R.string.asset_bi_dongjie)+doMoney)
//                        txt_vail_money.setText(factMoeny.toString())
//                        txt_vail_money.setText(factMoeny?.toBigDecimal()?.stripTrailingZeros()?.toPlainString())
                    } catch (E: Exception) {
                    }
                }
            }
            onFail { t: HomeAsset?, code: String, msg: String ->

            }
        }
    }


    fun getBalanceMoney(balance: String): BigDecimal {
        var b1 = java.math.BigDecimal(balance)
        if (b1.compareTo(BigDecimal("0")) == 0) {
            return BigDecimal("0")
        }
        return b1.divide(BigDecimal(Math.pow(10.0, Constant.DECIMALS.toDouble()).toString()))
    }


    fun getFatckMoney(reserveBase: String, reserveInc: String, ownerCount: String): BigDecimal {
        val b1 = java.math.BigDecimal(reserveBase)
        val b2 = java.math.BigDecimal(reserveInc)
        val b3 = java.math.BigDecimal(ownerCount)
        return b1.add(b2.multiply(b3))
            .divide(BigDecimal(Math.pow(10.0, Constant.DECIMALS.toDouble()).toString()))
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
                            if (xa < 0) {
                                txt_vail_money.setText("0")
                            } else {
//                                txt_vail_money.setText(b1.balance)
                                txt_vail_money.setText(
                                    b1.balance?.toBigDecimal()?.stripTrailingZeros()
                                        ?.toPlainString()
                                )
                            }
                        }
                    }
                }
            }
            onFail { t: AccountLines?, code: String, msg: String ->

            }
        }
    }


}