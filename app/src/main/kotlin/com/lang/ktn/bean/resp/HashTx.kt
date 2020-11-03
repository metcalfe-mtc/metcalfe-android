package com.lang.ktn.bean.resp

data class   HashTx(
    val account: String,
    val amount: Amount,
    val limitAmount: LimitAmount,
    val date: Long,
    val destination: String,
    val fee: String,
    val flags: Long,
    val hash: String,
    val ledgerIndex: Int,
    val memos: List<Memo>,
    val sequence: Int,
    val signingPubKey: String,
    val transactionResult: String,
    val transactionType: String,
    val txnSignature: String,
    val validated: Boolean
)