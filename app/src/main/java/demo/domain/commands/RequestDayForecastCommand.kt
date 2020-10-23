package com.antonioleiva.weatherapp.domain.commands


import com.antonioleiva.weatherapp.domain.model.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestDayForecastCommand(
        val id: Long,
        private val forecastProvider: Any ) :
        Command<Any> {

    override suspend fun execute() = withContext(Dispatchers.IO) {
//        forecastProvider.requestForecast(id)
    }
}