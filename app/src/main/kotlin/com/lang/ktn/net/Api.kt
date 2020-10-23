package com.lang.ktn.net

import android.util.Log
import com.lang.ktn.net.services.ApiServices
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

class Api {

    var service: ApiServices

    init {
        service = RetrofitUtil.getService(ApiServices::class.java)
    }

    companion object {
        val instance: Api by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Api()
        }
    }

//    suspend fun getTabDiscovery(@Url url: String ): String {
//        return withContext(Dispatchers.IO) {
//            try {
//                service.getTabDiscovery(url).await()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                throw e
//            }
//        }
//    }
//
//       suspend fun getTabDiscover1y(@Url url: String ):String?{
//
//          return withContext(Dispatchers.Main) {
//              try {
//                  service.getTabDiscovery(url).await()
//              } catch (e: Exception) {
//                  e.printStackTrace()
//                  throw e
//              }
//          }
//    }
//


}