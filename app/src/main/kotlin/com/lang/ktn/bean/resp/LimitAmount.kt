package com.lang.ktn.bean.resp

data class LimitAmount(
    val currency: String,
    val issuer: String,
    val value: String,
    val decimals: String
)