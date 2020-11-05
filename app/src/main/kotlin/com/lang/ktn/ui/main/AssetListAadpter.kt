package com.lang.ktn.ui.main

import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.antonioleiva.weatherapp.domain.model.Forecast
import com.antonioleiva.weatherapp.domain.model.ForecastList
import com.antonioleiva.weatherapp.ui.adapters.ForecastListAdapter
import com.lang.ktn.bean.resp.HashTx
import com.lang.ktn.utils.Constant
import com.lang.progrom.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_wallet_detail.*
import kotlinx.android.synthetic.main.item_asset_item.*
import kotlinx.android.synthetic.main.item_asset_recode.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class AssetListAadpter(
    private val currency: String,
    private val weekForecast: MutableList<HashTx>,
    private val itemClick: (HashTx) -> Unit
) : RecyclerView.Adapter<AssetListAadpter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_asset_recode, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weekForecast.size
    }

    fun getTextName(date: Long): String {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(Date(date))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = weekForecast[position]
        if ("Payment" == bean.transactionType) {
//            holder.itemView.isEnabled = true
            if (currency == bean.account) {
                holder.txt_churu.setText(R.string.zichan_list_zhuanchu)
                holder.txt_churu.setTextColor(holder.itemView.resources.getColor(R.color.trade_blue,null))
            } else {
                holder.txt_churu.setText(R.string.zichan_list_zhuanru)
                holder.txt_churu.setTextColor(Color.RED)
            }
        } else {
//            holder.itemView.isEnabled = false
            if ("0".equals(bean.limitAmount.value)) {
                holder.txt_churu.setText(R.string.main_asset_shouxin_cancel)
                holder.txt_churu.setTextColor(holder.itemView.resources.getColor(R.color.trade_blue,null))
            } else {
                holder.txt_churu.setText(R.string.main_asset_shouxin)
                holder.txt_churu.setTextColor(Color.RED)
            }
        }
        holder.txt_coin_time.setText(getTextName(bean.date))

        if ("tesSUCCESS" == bean.transactionResult) {
            holder.txt_coin_asset_s.setText(R.string.zichan_list_success)
        } else {
            holder.txt_coin_asset_s.setText(R.string.zichan_list_fail)
        }

        if ("Payment" == bean.transactionType) {
            holder.txt_icon_jine.visibility = VISIBLE

            if (TextUtils.isEmpty(bean.amount?.currency)) {
                var allMoeny =
                    bean.amount?.value?.toLong()!!
                        .div(Math.pow(10.0, bean.amount.decimals.toDouble()))
                        .toString()
                if (currency == bean.account) {
                    holder.txt_icon_jine.setText(
                        "- " + allMoeny.toBigDecimal().stripTrailingZeros().toPlainString()
                    ) // + Constant.CURRN
                } else {
                    holder.txt_icon_jine.setText(
                        "+ " + allMoeny.toBigDecimal().stripTrailingZeros().toPlainString()
                    ) // + Constant.CURRN
                }

            } else if ("M".equals(bean.amount?.currency)) {
                if (currency == bean.account) {
                    holder.txt_icon_jine.setText(
                        "- " + bean.amount?.value?.toBigDecimal().divide(BigDecimal("1000000"))
                            .stripTrailingZeros().toPlainString()
                    )// + bean.amount?.currency
                } else {
                    holder.txt_icon_jine.setText(
                        "+ " + bean.amount?.value?.toBigDecimal().divide(BigDecimal("1000000"))
                            .stripTrailingZeros().toPlainString()
                    )// + bean.amount?.currency
                }
            } else {
                if (currency == bean.account) {
                    holder.txt_icon_jine.setText(
                        "- " + bean.amount?.value?.toBigDecimal().stripTrailingZeros()
                            .toPlainString()
                    )// + bean.amount?.currency
                } else {
                    holder.txt_icon_jine.setText(
                        "+ " + bean.amount?.value?.toBigDecimal().stripTrailingZeros()
                            .toPlainString()
                    )// + bean.amount?.currency
                }
            }
        }else{
            holder.txt_icon_jine.visibility = INVISIBLE
        }
        holder.itemView.setOnClickListener {
            itemClick(bean)
        }
    }


    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
    }

    fun lockx(p1: String, p2: String) {

    }

    fun lock(
        p1: String,
        p2: String,
        method: (str1: String, str2: String) -> Unit,
        methodx: (str1: String, str2: String) -> Unit
    ): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        lock("dsad","dasd",this::lockx,this::lockx);
        return p1
    }


}