package com.lang.ktn.bean


import com.google.gson.annotations.SerializedName

data class OrderGuadan(
    @SerializedName("account")
    var account: String,
    @SerializedName("ledgerIndex")
    var ledgerIndex: Int,
    @SerializedName("marker")
    var marker: String,
    @SerializedName("offers")
    var offers: ArrayList<OfferOrder>
)
data class OfferOrder(
    @SerializedName("amount")
    var amount: AmountOrder,
    @SerializedName("flags")
    var flags: Long,
    @SerializedName("offerType")
    var offerType: String,
    @SerializedName("price")
    var price: PriceOrder,
    @SerializedName("sequence")
    var sequence: Int,
    @SerializedName("total")
    var total: TotalOrder
)

data class TotalOrder(
    @SerializedName("currency")
    var currency: String,
    @SerializedName("decimals")
    var decimals: Int,
    @SerializedName("issuer")
    var issuer: String,
    @SerializedName("value")
    var value: String
)

data class PriceOrder(
    @SerializedName("currency")
    var currency: String,
    @SerializedName("decimals")
    var decimals: Int,
    @SerializedName("issuer")
    var issuer: String,
    @SerializedName("value")
    var value: String
)

data class AmountOrder(
    @SerializedName("currency")
    var currency: String,
    @SerializedName("decimals")
    var decimals: Int,
    @SerializedName("issuer")
    var issuer: String,
    @SerializedName("value")
    var value: String
)

