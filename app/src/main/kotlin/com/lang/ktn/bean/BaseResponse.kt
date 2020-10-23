package com.lang.ktn.bean


open class BaseResponse<T>{
    var code: String= ""
    var message: String=""
    var data:T?=null
}