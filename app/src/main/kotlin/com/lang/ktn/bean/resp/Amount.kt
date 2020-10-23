package com.lang.ktn.bean.resp

data class Amount(
    val currency: String,
    val issuer: String,
    val value: String,
    val decimals: String
)