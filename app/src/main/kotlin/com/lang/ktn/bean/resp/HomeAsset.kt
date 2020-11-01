package com.lang.ktn.bean.resp

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


class HomeAsset() : Parcelable{
    var currency: String? = "";
    var account: String? = "";
    var balance: String? = "";
    var baseFee: String? = "";
    var decimals: Int?=0
    var decimalsLimit: Int?=0
    var flags: Int ?=0
    var ledgerIndex: Int?=0
    var ownerCount: Int?=0
    var reserveBase: String?="0"
    var reserveInc: String?="0"
    var sequence: Long?=null

    constructor(parcel: Parcel) : this() {
        currency = parcel.readString()
        account = parcel.readString()
        balance = parcel.readString()
        baseFee = parcel.readString()
        decimals = parcel.readValue(Int::class.java.classLoader) as? Int
        decimalsLimit = parcel.readValue(Int::class.java.classLoader) as? Int
        flags = parcel.readValue(Int::class.java.classLoader) as? Int
        ledgerIndex = parcel.readValue(Int::class.java.classLoader) as? Int
        ownerCount = parcel.readValue(Int::class.java.classLoader) as? Int
        reserveBase = parcel.readString()
        reserveInc = parcel.readString()
        sequence = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currency)
        parcel.writeString(account)
        parcel.writeString(balance)
        parcel.writeString(baseFee)
        parcel.writeValue(decimals)
        parcel.writeValue(decimalsLimit)
        parcel.writeValue(flags)
        parcel.writeValue(ledgerIndex)
        parcel.writeValue(ownerCount)
        parcel.writeString(reserveBase)
        parcel.writeString(reserveInc)
        parcel.writeValue(sequence)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeAsset> {
        override fun createFromParcel(parcel: Parcel): HomeAsset {
            return HomeAsset(parcel)
        }

        override fun newArray(size: Int): Array<HomeAsset?> {
            return arrayOfNulls(size)
        }
    }


}