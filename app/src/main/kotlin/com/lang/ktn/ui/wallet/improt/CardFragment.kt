package com.lang.ktn.ui.wallet.improt

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast.makeText
import androidx.fragment.app.FragmentActivity
import com.lang.ktn.base.BaseFragment
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.QrUtils
import com.lang.ktn.utils.changeEyes
import com.lang.ktn.utils.toast
import com.lang.progrom.R
import kotlinx.android.synthetic.main.fragment_zxing.*
import kotlinx.android.synthetic.main.fragment_zxing.btn_mdy_pk
import kotlinx.android.synthetic.main.fragment_zxing.edt_newpsd_1
import kotlinx.android.synthetic.main.fragment_zxing.edt_newpsd_2
import kotlinx.android.synthetic.main.fragment_zxing.edt_sercert
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class CardFragment:BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_zxing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edt_newpsd_1.onCreateWallte()
        edt_newpsd_2.onCreateWallte()
        edt_sercert.onCreateWallte()


        lookPwd_zhujici1.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_1,isChecked) }
        lookPwd_zhujici2.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_2,isChecked) }

        layour_scan.setOnClickListener {
            methodRequiresTwoPermission()
        }
        btn_mdy_pk.setOnClickListener {
            val oldPsd1 = edt_newpsd_1.text.toString().trim()
            val oldPs2 = edt_newpsd_2.text.toString().trim()

            val sercert = edt_sercert.text.toString().trim()
            if(oldPsd1!=oldPs2){
               toast(getString(R.string.import_wallet_psd_error))
                return@setOnClickListener
            }
           var  cur: FragmentActivity? =activity;
            if(cur is ImportWalletActivity){
                cur.secretAddress(oldPsd1,sercert)
            }
        }
    }




    val RC_CAMERA_PERM = 123
    @AfterPermissionGranted(value = 123)
    fun methodRequiresTwoPermission() {
        val perms = arrayOf<String>(Manifest.permission.CAMERA)
        if (activity?.let { EasyPermissions.hasPermissions(it, *perms) }!!) {
            QrUtils.getInstance(activity!!).startScan(activity) { edt_sercert.setText(it) }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.import_wallet_carmar_p), RC_CAMERA_PERM, *perms)
        }
    }


    fun TextView.onCreateWallte(): Unit {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val oldPsd1 = edt_newpsd_1.text.toString().trim()
                val oldPs2 = edt_newpsd_2.text.toString().trim()
                val sercert = edt_sercert.text.toString().trim()

                btn_mdy_pk.isEnabled = oldPsd1.length >= Constant.PSDSIZE  && oldPs2.length >= Constant.PSDSIZE  && sercert.length>=Constant.PSDSIZE
            }
        })
    }


}