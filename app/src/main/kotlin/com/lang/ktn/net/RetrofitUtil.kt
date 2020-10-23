package com.lang.ktn.net

import android.os.Build
import android.provider.SyncStateContract
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lang.ktn.net.stringconvert.StringConverterFactory
import com.lang.progrom.BuildConfig
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class RetrofitUtil {


    companion object {
        @Volatile var mRetrofit: Retrofit? = create()
        /**
         * 创建Retrofit
         */
        fun create(): Retrofit {
            //日志显示级别
            val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
            //新建log拦截器
            val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = level
            // okHttpClientBuilder
            val okHttpClientBuilder = OkHttpClient().newBuilder()

            okHttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS)
            //OkHttp进行添加拦截器loggingInterceptor
            okHttpClientBuilder.addInterceptor(loggingInterceptor)

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClientBuilder.build())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(StringConverterFactory()) // 使用Gson作为数据转换器
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build()
        }

//        val retrofitService: RetrofitService = RetrofitUtil.getService(SyncStateContract.Constants.REQUEST_BASE_URL, RetrofitService::class.java)

        /**
         * 获取ServiceApi
         */
        fun <T> getService(service: Class<T>): T {
            if(mRetrofit==null){
                mRetrofit= create()
            }
            Log.e("api","@mRetrofit"+ mRetrofit)
            return mRetrofit!!.create(service)
        }
    }



//    private fun click() = runBlocking {
//        GlobalScope.launch(Dispatchers.Main) {
//            var coroutine:String? = null
//            coroutine = GlobalScope.async(Dispatchers.IO) {
//                // 比如进行了网络请求
//                // 放回了请求后的结构
//                return@async "main"
//            }.await()
//        }
//    }

}
