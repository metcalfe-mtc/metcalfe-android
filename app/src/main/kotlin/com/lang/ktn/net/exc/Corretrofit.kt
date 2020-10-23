package com.lang.ktn.net.exc

import com.lang.ktn.net.RetrofitNet
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.ConnectException

fun <T> CoroutineScope.retrofitApi(dsl: RetrofitNet<T>.() -> Unit) {
    //在主线程中开启协程
    this.launch(Dispatchers.Main) {
        val coroutine = RetrofitNet<T>().apply(dsl)
        coroutine.api?.let { call ->
            //async 并发执行 在IO线程中
            val deferred = async(Dispatchers.IO) {
                try {
                    call.execute() //已经在io线程中了，所以调用Retrofit的同步方法
                } catch (e: ConnectException) {
                    coroutine.onFail?.invoke("网络连接出错", "-100")
                    null
                } catch (e: Exception) {
                    coroutine.onFail?.invoke("未知网络错误", "-100")
                    null
                }
            }
            //当协程取消的时候，取消网络请求
            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                    coroutine.clean()
                }
            }
            //await 等待异步执行的结果
            val response = deferred.await()
            if (response == null) {
                coroutine.onFail?.invoke("返回为空", "-100")
            } else {
                if(response.isSuccessful){
                    val  bean=response.body();
                    coroutine.onSuccess?.invoke(bean)
                }else{
                    coroutine.onFail?.invoke(response.message(), response.code().toString())
                }
            }
            coroutine.onComplete?.invoke()
        }
    }
}



//                var x1=response;
//                var x2=response.body();
//
//                if (x2 is BaseResponse){
//                    response.let {
////                        if (response.isSuccessful) {
////                            //访问接口成功
////                            if (response.code == "1") {
////                                //判断status 为1 表示获取数据成功
////                                coroutine.onSuccess?.invoke(response.body())
////                            } else {
////                                coroutine.onFail?.invoke(response.data==null: "返回数据为空", response.code())
////                            }
////                        } else {
////                            coroutine.onFail?.invoke(response.errorBody().toString(), response.code())
////                        }
//                    }
//
//                }else{
//                    response.let {
//                        if (response.isSuccessful) {
//                            coroutine.onSuccess?.invoke(response.body()!!)
//                        } else {
//                            coroutine.onFail?.invoke(response.body()?.toString()?: "返回数据为空", response.code().toString())
//                        }
//                    }
//
//                }
