package com.lang.ktn.net

import com.lang.ktn.bean.BaseResponse
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call

class RetrofitNet<T>{

    var api: (Call<T>)? = null

    // 项目中的其他网络请求接口处理
    internal var onSuccess: ((T?) -> Unit)? = null
        private set
    internal var onFail: ((String,String) -> Unit)? = null
        private set

    internal var onComplete: (() -> Unit)? = null
        private set


    /**
     * 获取数据成功
     * @param block (T) -> Unit
     */
    fun onSuccess(block: (T?) -> Unit) {
        this.onSuccess = block
    }

    /**
     * 获取数据失败
     * @param block (msg: String, errorCode: Int) -> Unit
     */
    fun onFail(block: (msg: String, errorCode: String) -> Unit) {
        this.onFail = block
    }
    /**
     * 访问完成
     * @param block () -> Unit
     */
    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    internal fun clean() {
        onSuccess = null
        onComplete = null
        onFail = null
    }


}