package com.lang.ktn.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class TxJson{
    var TransactionType:String?=null;
    var Account:String?=null;
    var Destination:String?=null;
    var Amount:String?=null;
    var Fee:String?=null;
    var Sequence:Int?=null;
    var Memos:List<HashMap<String,Map<String,String>>>?=null;
}