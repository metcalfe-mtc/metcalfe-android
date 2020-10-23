package com.antonioleiva.weatherapp.domain.commands


import com.antonioleiva.weatherapp.domain.model.ForecastList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestForecastCommand(
        private val zipCode: Long,
        private val forecastProvider:Any) :
        Command<Any> {

    companion object {
        const val DAYS = 7
    }

    override suspend fun execute() = withContext(Dispatchers.IO) {
//        forecastProvider.requestByZipCode(zipCode, DAYS)
    }
}