package com.lang.ktn.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.antonioleiva.weatherapp.domain.model.Forecast
import com.antonioleiva.weatherapp.domain.model.ForecastList
import com.antonioleiva.weatherapp.ui.adapters.ForecastListAdapter
import com.lang.ktn.bean.resp.HomeAsset
import com.lang.progrom.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_asset_item.*
import java.math.BigDecimal

class AssetAadpter(
    private val weekForecast: List<HomeAsset>,
    private val itemClick: (HomeAsset) -> Unit
) : RecyclerView.Adapter<AssetAadpter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_asset_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return weekForecast.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(weekForecast[position])
    }


    class ViewHolder(override val containerView: View, private val itemClick: (HomeAsset) -> Unit) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindForecast(forecast: HomeAsset) {
            forecast?.let {
                txt_coin.setText(it.currency)
                txt_coin_asset.setText(
                    it.balance?.toBigDecimal()?.stripTrailingZeros()?.toPlainString()
                )
                itemView.setOnClickListener { itemClick(forecast) }
            }
        }
    }


}