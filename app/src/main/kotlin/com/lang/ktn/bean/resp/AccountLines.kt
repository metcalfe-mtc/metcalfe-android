package com.lang.ktn.bean.resp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class AccountLines(
    val account: String,
    val ledgerIndex: Int,
    val lines: ArrayList<HomeAsset>
): Parcelable