package com.lang.ktn.bean.respws

data class ResultOrder(
    val asks: List<WsOrder>,
    val bids: List<WsOrder>
)