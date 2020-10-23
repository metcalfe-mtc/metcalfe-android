package com.lang.ktn.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Model(val title: String, val amount: Int) : Parcelable