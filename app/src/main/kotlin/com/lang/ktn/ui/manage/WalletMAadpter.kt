package com.lang.ktn.ui.manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.progrom.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_asset_icon.*

class WalletMAadpter(private val weekForecast:  List<SqlAddres>, private val itemClick: (SqlAddres) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset_icon, parent, false)
            return ViewItemHolder(view, itemClick)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun getItemCount(): Int {
       return weekForecast?.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewItemHolder) {
            holder.bindForecast(weekForecast[position])
        }
    }


    class ViewItemHolder(override val containerView: View, private val itemClick: (SqlAddres) -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindForecast(forecast: SqlAddres) {

            if("1"==forecast.afault) {
                layout_bg_q.setBackgroundResource(R.drawable.shape_curre_bg_sel)
            }else{
                layout_bg_q.setBackgroundResource(R.drawable.shape_curre_bg)
            }

            item_text_name.setText(forecast.nickName)
            item_text_addres.setText(forecast.address)
            with(forecast) {
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }





}