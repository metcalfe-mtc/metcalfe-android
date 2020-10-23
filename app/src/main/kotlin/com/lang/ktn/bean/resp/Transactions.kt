package com.lang.ktn.bean.resp

data class Transactions(
    val account: String,
    val marker: String,
    val transactions: List<HashTx>
)