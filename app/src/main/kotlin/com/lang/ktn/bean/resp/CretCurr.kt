package com.lang.ktn.bean.resp

data class CretCurr(
    val currency: String,
    val imgUrl: String,
    val issuer: String,
    var value: String,
    var money: String,
    var shouxin:Boolean =false
)