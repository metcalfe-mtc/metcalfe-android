package com.lang.ktn.ui.wallet.shouxin

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lang.ktn.base.BaseActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_magni_walletasset.*
import kotlinx.android.synthetic.main.activity_magni_walletasset_list.*
import kotlinx.android.synthetic.main.nav_layout.*
//授信记录
class WalletRecodeActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magni_walletasset_list)
        nav_tittle.setText("记录")
        nav_img_left.setOnClickListener{ finish() }

        val lay= androidx.recyclerview.widget.LinearLayoutManager(this)
        lay.orientation= LinearLayoutManager.VERTICAL
        recycle_addset_code.layoutManager=lay
        recycle_addset_code.adapter= MageAssetCodeAadpter(6) {
            makeText("tex: " + it)
        }
    }



}