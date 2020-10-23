package com.lang.ktn.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.nav_layout.*

class AddAssetActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_walletasset)
        nav_tittle.setText(getString(R.string.ad_title_info))
        nav_img_left.setOnClickListener{ finish() }
    }

    fun onCreateWallte(view: View) {
        val intent = Intent(this, BackupsActivity::class.java)
        startActivity(intent)
    }


}