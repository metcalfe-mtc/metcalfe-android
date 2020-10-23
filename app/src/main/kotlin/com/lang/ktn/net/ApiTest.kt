package com.lang.ktn.net

import android.util.Log
import com.lang.ktn.net.services.ApiServices
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

class ApiTest {

    var service: ApiServices

    init {
        service = RetrofitUtil.getService(ApiServices::class.java)
    }

    /**
     * 单例
     */
    companion object {
        val instance: ApiTest by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiTest()
        }
    }

    //请求类型 + 路由

    suspend fun getTabDiscovery(@Url url: String ): Deferred<String> {
        return  service.getTabDiscovery(url);
    }



}