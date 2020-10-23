package com.lang.ktn.ui.wallet.improt

import android.Manifest
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.lang.ktn.base.BaseFragment
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.QrUtils
import com.lang.ktn.utils.changeEyes
import com.lang.ktn.utils.toast
import com.lang.progrom.R
import kotlinx.android.synthetic.main.fragment_zhujici.*
import java.util.*


class ZxingFragment: BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_zhujici, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edt_newpsd_1.onCreateWallte()
        edt_newpsd_2.onCreateWallte()
        edt_sercert.onCreateWallte()

        lookPwd_zhujici1.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_1,isChecked) }
        lookPwd_zhujici2.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_2,isChecked) }

        btn_mdy_pk.setOnClickListener {
            val oldPsd1 = edt_newpsd_1.text.toString().trim()
            val oldPs2 = edt_newpsd_2.text.toString().trim()

            val sercert = edt_sercert.text.toString().trim()
            if(oldPsd1!=oldPs2){
                toast(getString(R.string.import_wallet_psd_error))
                return@setOnClickListener
            }
            var st1 = StringTokenizer(sercert)
            var infokey:String=""
            while (st1.hasMoreTokens()) {
                infokey=infokey+st1.nextToken()+" "
            }
            infokey=infokey.trim()
            var  cur: FragmentActivity? =activity;
            if(cur is ImportWalletActivity){
                cur.zhujici(oldPsd1,infokey)
            }
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