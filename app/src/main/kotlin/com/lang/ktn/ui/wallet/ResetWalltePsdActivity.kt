package com.lang.ktn.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.nav_layout.*

class ResetWalltePsdActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_walletpsd)
        nav_tittle.setText("重设交易密码")
        nav_img_left.setOnClickListener{ finish() }
    }

    fun onCreateWallte(view: View) {
        val intent = Intent(this, BackupsActivity::class.java)
        startActivity(intent)
    }


}