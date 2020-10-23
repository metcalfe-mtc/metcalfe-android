package com.lang.ktn.ui.wallet.shouxin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lang.ktn.bean.resp.CretCurr
import com.lang.ktn.bean.resp.HomeAsset
import com.lang.progrom.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_asset.*

class MageAssetAadpter(private val weekForecast: ArrayList<CretCurr>,private val lines: ArrayList<HomeAsset>?,private val itemClick: (CretCurr) -> Unit) :
    RecyclerView.Adapter<MageAssetAadpter.ViewHolder>() {

    init {

        if(weekForecast!=null) {
            if (lines != null) {
                for (mIndex in weekForecast.indices) {
                    val bean=weekForecast[mIndex]
                    for (index in lines.indices) {
                        if (lines[index].currency == bean.currency) {
                            bean.shouxin = true
                            bean.money= lines[index].balance.toString()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
       return weekForecast.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bean=weekForecast[position];
        holder.itemView.setOnClickListener { itemClick(bean) }
        holder. txt_name.setText(bean.currency)
        holder.txt_key.setText(bean.issuer)
        Glide.with(holder.img_flag_X).load(bean.imgUrl).override(46,46).into(holder.img_flag_X);
        if(bean.shouxin){
            holder.img_kaiguan.setImageResource(R.mipmap.icon_choose_open)
        }else{
            holder.img_kaiguan.setImageResource(R.mipmap.icon_choose_close)
        }
    }


    class ViewHolder(override val containerView: View, private val itemClick: (CretCurr) -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
    }

    fun lockx(p1: String, p2: String) {

    }
    fun lock(p1: String, p2: String, method: (str1: String, str2: String) -> Unit,methodx: (str1: String, str2: String) -> Unit): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        lock("dsad","dasd",this::lockx,this::lockx);
        return p1
    }



}