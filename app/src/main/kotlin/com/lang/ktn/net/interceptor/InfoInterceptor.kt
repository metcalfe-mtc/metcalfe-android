package com.lang.ktn.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response
//https://www.jianshu.com/p/b58555b47991
class InfoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();
        return chain.proceed(request);
    }
}