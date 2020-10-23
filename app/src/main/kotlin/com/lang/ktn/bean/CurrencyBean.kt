package com.lang.ktn.bean




data class CurrencyBean(var currency:String,var issuer:String,var value:String) : java.io.Serializable


data class orderResultBean(var bidAmount:CurrencyBean,var askAmount:CurrencyBean): java.io.Serializable


data class Transactions(
    var hash:String,
    var account:String,
    var fee:String,
    var transactionType:String,
    var transactionResult:String,
    var date:Long,
    var offerType:String,
    var clinchAmount:CurrencyBean,
    var clinchTotal:CurrencyBean,
    var cancelAmount:CurrencyBean,
    var bidAmount:CurrencyBean,
    var askAmount:CurrencyBean,
    var orderResults:List<orderResultBean>
): java.io.Serializable

data class TransactionsLista(var transactions:ArrayList<Transactions>,var marker:String="")