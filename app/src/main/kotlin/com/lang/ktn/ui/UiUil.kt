package com.lang.ktn.ui

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Point
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.lang.ktn.utils.Constant
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_assets_zhuancuh.*
import kotlinx.android.synthetic.main.activity_modify_walletpsd.*

// 密码 弹出框
fun showDialogSelPsd(activity:Activity,onPsd:(psd:String,dialog: Dialog,txt_infoView:TextView)->Unit){
    var dialog= Dialog(activity, R.style.Dialog)
    dialog.setContentView(R.layout.dialog_layout_pwd2)
    dialog.show()
    var edtPsdView=dialog.findViewById<EditText>(R.id.et_password);
    var txt_infoView=dialog.findViewById<TextView>(R.id.txt_info);
    var btn=dialog.findViewById<Button>(R.id.confirm);
    edtPsdView.onCreateWallte(btn)
    dialog.findViewById<TextView>(R.id.tv_forget).setOnClickListener {
        dialog.dismiss()
    }
    var box= dialog.findViewById<Button>(R.id.lookPwd_show) as CheckBox
    box.setOnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            edtPsdView.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            edtPsdView.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        edtPsdView.setSelection(edtPsdView.length())
    }
    dialog.findViewById<Button>(R.id.confirm).setOnClickListener {
        onPsd(edtPsdView.text.toString().trim(),dialog,txt_infoView);
    }
    val windowManager = activity.windowManager
    val display = windowManager.defaultDisplay
    val win = dialog.getWindow()
    val lp = win!!.getAttributes()
    val point = Point()
    display.getSize(point)
    val margin: Int=0
    if (margin > -1) {
        lp.width = point.x - margin * 2
    } else {
        lp.width = point.x
    }

    lp.gravity = Gravity.BOTTOM

    win!!.setAttributes(lp)
    win!!.setDimAmount(0.5f)
    dialog.setOnDismissListener(object : DialogInterface.OnDismissListener{
        override fun onDismiss(dialog: DialogInterface){
            win!!.setDimAmount(1f)
        }
    })
}

fun TextView.onCreateWallte(confirm:Button): Unit {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val oldPsd1 = s.toString().trim()
            confirm.isEnabled = oldPsd1.length >= Constant.PSDSIZE
        }
    })
}



