package com.lang.ktn.ui.manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.progrom.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_asset_icon.*

class Wallet2Aadpter(private val weekForecast:  List<SqlAddres>, private val itemClick: (SqlAddres) -> Unit, private val addClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM:Int=1
    val ITEM_ADD:Int=3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(ITEM==viewType){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset_icon, parent, false)
            return ViewItemHolder(view, itemClick)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset_icon_add, parent, false)
            return ViewAddHolder(view, addClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(weekForecast.size==position){
            return ITEM_ADD;
        }
        return ITEM
    }
    override fun getItemCount(): Int {
       return weekForecast?.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewItemHolder) {
            holder.bindForecast(weekForecast[position])
        }else   if(holder is ViewAddHolder) {
            holder.bindForecast(position)
        }
    }


    class ViewItemHolder(override val containerView: View, private val itemClick: (SqlAddres) -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindForecast(forecast: SqlAddres) {
            forecast?.let {
                item_text_name.setText(it.nickName)
                item_text_addres.setText(it.address)
                if("1"==forecast.afault) {
                    layout_bg_q.setBackgroundResource(R.drawable.shape_curre_bg_sel)
                }else{
                    layout_bg_q.setBackgroundResource(R.drawable.shape_curre_bg)
                }
            }
            with(forecast) {
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

    class ViewAddHolder(override val containerView: View, private val addClick: () -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindForecast(forecast: Int) {
            with(forecast) {

                itemView.setOnClickListener { addClick() }
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