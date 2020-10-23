package com.lang.ktn.net.services

import com.google.gson.JsonElement
import com.lang.ktn.bean.*
import com.lang.ktn.bean.resp.*
import com.lang.ktn.bean.resp.Transactions
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {



    //请求类型 + 路由
    @GET("api/v5/index/tab/discovery")
    suspend  fun getTabDiscovery(@Url url: String ): Deferred<String>//由于json采用手动解，所以没有用泛型

    //请求类型 + 路由
    @GET("api/v5/index/tab/discovery")
    fun getTabDiscoverxy(@Url url: String ): Deferred<String>//由于json采用手动解，所以没有用泛型

    //请求类型 + 路由
    @GET("api/v5/index/tab/discovery")
    suspend  fun getTabDiscovery1(@Url url: String ): Deferred<BaseResponse<Transactions>>//由于json采用手动解，所以没有用泛型






    // 获取主资产
    @GET("api/core/account/info")
    fun accountInfo(@QueryMap map:Map<String,String>): Call<BaseResponse<HomeAsset>>
    // 获取我的资产情况
    @GET("api/core/account/lines")
    fun accountLines(@QueryMap map:Map<String,String>): Call<BaseResponse<AccountLines>>


    //交易历史记录
    @GET("api/core/transaction/account")
    fun transactionAccount(@QueryMap map:Map<String,String>): Call<BaseResponse<Transactions>>

    //交易历史记录(获取指定账户转账历史列表)
    @GET("api/custom/payment/paymentHistoryList")
    fun paymentHistoryList(@QueryMap map:Map<String,String>): Call<BaseResponse<Transactions>>

    //交易历史记录
    @GET("api/custom/basic/get_version")
    fun getVersion(@QueryMap map:Map<String,String>): Call<BaseResponse<Version>>



    // 获取授信列表
    @GET("api/core/transaction/tx")
    fun transactionTx(@Query("hash") hash:String): Call<BaseResponse<HashTx>>



    // 获取授信列表
    @GET("api/custom/token/list")
    fun tokenList(): Call<BaseResponse<ArrayList<CretCurr>>>

    //提交各种签名的Hash & txtBlcok
    @POST("api/core/transaction/submit")
    fun transactionSubmit(@Body data:Any): Call<BaseResponse<TraBeam>>


    // 获取市场列表
    @GET("api/custom/exchange/marketList")
    fun marketList(): Call<BaseResponse<java.util.ArrayList<PartBean>>>

    // 根据价格买卖类型构建txJson
    @GET("api/custom/exchange/buildOffer")
    fun buildOffer(@QueryMap map:Map<String,String>): Call<BaseResponse<JsonElement>>

    // 获取指定账户指定交易对满足过滤条件的挂单历史列表
    @GET("api/custom/exchange/orderHistoryList")
    fun orderHistoryList(@QueryMap map:Map<String,String>): Call<BaseResponse<TransactionsLista>>


    // 获取指定账户挂单列表
    @GET("api/custom/exchange/orderList")
    fun orderList(@QueryMap map:Map<String,String>): Call<BaseResponse<OrderGuadan>>


}