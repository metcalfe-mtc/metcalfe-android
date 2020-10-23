package com.lang.ktn.ui.manage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_manageicon.*
import kotlinx.android.synthetic.main.nav_layout.*


class ManageIconActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manageicon)
        nav_tittle.setText(getString(R.string.wallet_list_title))
        nav_img_left.setOnClickListener{ finish() }

        val lay= androidx.recyclerview.widget.LinearLayoutManager(this)
        lay.orientation= LinearLayoutManager.VERTICAL
        recycle_list.layoutManager=lay
        recycle_list.adapter= MageAadpter(6){
            makeText("tex: "+it)
        }

    }

    fun onCreateWallte(view: View) {
        val intent = Intent(this, BackupsActivity::class.java)
        startActivity(intent)
    }


}