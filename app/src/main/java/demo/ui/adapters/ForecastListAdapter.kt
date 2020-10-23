package com.antonioleiva.weatherapp.ui.adapters

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.antonioleiva.weatherapp.domain.model.Forecast
import com.antonioleiva.weatherapp.domain.model.ForecastList
import com.antonioleiva.weatherapp.extensions.ctx
import com.antonioleiva.weatherapp.extensions.toDateString
import kotlinx.android.extensions.LayoutContainer


class ForecastListAdapter(private val weekForecast: ForecastList,
        private val itemClick: (Forecast) -> Unit) :
        RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(Button(parent.context), itemClick)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(weekForecast[position])
    }

    override fun getItemCount() = weekForecast.size

    class ViewHolder(override val containerView: View, private val itemClick: (Forecast) -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindForecast(forecast: Forecast) {
            with(forecast) {
//                Picasso.with(itemView.ctx).load(iconUrl).into(icon)
//                dateText.text = date.toDateString()
//                descriptionText.text = description
//                maxTemperature.text = "${high}º"
//                minTemperature.text = "${low}º"
//                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}