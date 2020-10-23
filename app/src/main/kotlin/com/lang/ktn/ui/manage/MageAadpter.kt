package com.lang.ktn.ui.manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lang.progrom.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_asset_item.*

class MageAadpter(private val weekForecast: Int, private val itemClick: (Int) -> Unit) : RecyclerView.Adapter<MageAadpter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
       return weekForecast
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(position)
    }


    class ViewHolder(override val containerView: View, private val itemClick: (Int) -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindForecast(forecast: Int) {
            with(forecast) {
                txt_coin.append( "${forecast}")
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

    fun lockx(p1: String, p2: String) {

    }
    fun lock(p1: String, p2: String, method: (str1: String, str2: String) -> Unit,methodx: (str1: String, str2: String) -> Unit): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        lock("dsad","dasd",this::lockx,this::lockx);
        return p1
    }



}