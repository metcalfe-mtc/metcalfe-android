package com.lang.ktn.ui.wallet.zhujici

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.WalletBean
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_create_wallet.*
import kotlinx.android.synthetic.main.nav_layout.*

class BackupsActivity : BaseActivity() {
    var wallet: WalletBean?=null
    var zhujici: Boolean?=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_wallet_backups)
        nav_tittle.setText(getString(R.string.back_title))
        nav_img_left.setOnClickListener{ finish() }

        wallet=intent.getParcelableExtra("data")
        zhujici=intent.getBooleanExtra("zhujici",false)
    }


    fun onNextWallte(view: View) {
//        val intent = Intent(this, RememberActivity::class.java)
        val intent = Intent(this, CopyWorldActivity::class.java)
        intent.putExtra("data",wallet)
        intent.putExtra("zhujici", zhujici)
        startActivity(intent)
    }


}