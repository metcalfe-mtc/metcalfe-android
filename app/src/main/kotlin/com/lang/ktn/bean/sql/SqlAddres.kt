package com.lang.ktn.bean.sql

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "account_table")
class SqlAddres() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "nickName", typeAffinity = ColumnInfo.TEXT)
    var nickName: String? = null

    @ColumnInfo(name = "address", typeAffinity = ColumnInfo.TEXT)
    var address: String? = null


    @ColumnInfo(name = "key", typeAffinity = ColumnInfo.TEXT)
    var key: String? = ""


    @ColumnInfo(name = "afault", typeAffinity = ColumnInfo.TEXT)
    var afault: String? = "0"

    @Ignore
    var zhujici: String = ""
    @Ignore
    var secret: String?= ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        nickName = parcel.readString()
        address = parcel.readString()
        key = parcel.readString()
        afault = parcel.readString()
        zhujici = parcel.readString()
        secret = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(nickName)
        parcel.writeString(address)
        parcel.writeString(key)
        parcel.writeString(afault)
        parcel.writeString(zhujici)
        parcel.writeString(secret)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SqlAddres> {
        override fun createFromParcel(parcel: Parcel): SqlAddres {
            return SqlAddres(parcel)
        }

        override fun newArray(size: Int): Array<SqlAddres?> {
            return arrayOfNulls(size)
        }
    }


}
