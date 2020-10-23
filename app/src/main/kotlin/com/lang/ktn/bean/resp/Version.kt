package com.lang.ktn.bean.resp

data class Version(
    val code: String,
    val desc: String,
    val force: Boolean,
    val upgrade: Boolean,
    val url: String,
    val version: String
)
