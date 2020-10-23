package com.lang.ktn.bean.respws

data class WsOrder(
    val amount: Amount,
    val price: Price
)
data class Amount(
    val currency: String,
    val issuer: String,
    val value: String
)
data class Price(
    val currency: String,
    val issuer: String,
    val value: String
)