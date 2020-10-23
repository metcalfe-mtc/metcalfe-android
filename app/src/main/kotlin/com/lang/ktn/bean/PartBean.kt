package com.lang.ktn.bean

data class PartBean(
    val baseCurrency: String,
    val baseIssuer: String,
    val baseDecimals: Int,
    val currency: String,
    val decimals: Int,
    val issuer: String
): java.io.Serializable