package com.lang.ktn.utils

import android.app.Activity
import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.lang.ktn.base.BaseAppliton.Companion.application
import java.text.SimpleDateFormat
import java.util.*

//fun <T> T.toString():String{
//    return this.toString()
//}

fun Context.toast(msg:String){
    ToastUtil.show(this, msg)
//    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}
fun Context.toast(msg:Int){
    ToastUtil.show(this,this.getString(msg))
//    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}
fun Fragment.toast(msg:String){
    ToastUtil.show(this.activity!!.application, msg)
//    Toast.makeText(activity,msg,Toast.LENGTH_LONG).show()
}
fun Activity.toast(msg:String){
    ToastUtil.show(this.application,msg)
}
fun Fragment.toast(msg:Int){
    ToastUtil.show(this.activity!!.application, this.getString(msg))
//    Toast.makeText(activity,msg,Toast.LENGTH_LONG).show()
}
fun Activity.toast(msg:Int){
    ToastUtil.show(this,this.getString(msg))
//    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}


fun Activity.changeEyes(view: EditText, isChecked: Boolean) {
    if (isChecked) {
        view.transformationMethod = HideReturnsTransformationMethod.getInstance()
    } else {
        view.transformationMethod = PasswordTransformationMethod.getInstance()
    }
    view.setSelection(view.length())
}


fun Fragment.changeEyes(view: EditText, isChecked: Boolean) {
    if (isChecked) {
        view.transformationMethod = HideReturnsTransformationMethod.getInstance()
    } else {
        view.transformationMethod = PasswordTransformationMethod.getInstance()
    }
    view.setSelection(view.length())
}

fun Any.toJsonString():String{
    return Gson().toJson(this)
}

fun Long.toFormatTime():String{
//    var data=Date(this.div(1000))
    var data=Date(this*1000)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return sdf.format(data)
}



