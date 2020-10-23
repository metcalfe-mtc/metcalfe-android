package com.lang.ktn.ui.wallet.zhujici

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lang.ktn.base.BaseActivity
import kotlinx.android.synthetic.main.nav_layout.*
import android.widget.TextView
import com.lang.ktn.bean.WalletBean
import com.lang.progrom.R
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.FlowLayout

import com.zhy.view.flowlayout.TagFlowLayout

import com.zhy.view.flowlayout.TagView
import kotlinx.android.synthetic.main.activity_wallet_copy_backremember.*


class CopyWorldActivity : BaseActivity() {
    lateinit var mInflater: LayoutInflater ;
    lateinit var onOriData:ArrayList<String>;
    var wallet: WalletBean?=null
    var zhujici: Boolean?=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.lang.progrom.R.layout.activity_wallet_copy_backremember)
        nav_tittle.setText(getString(R.string.back_ac_ok_title))
        nav_img_left.setOnClickListener{ finish() }

        mInflater = LayoutInflater.from(this);
        onOriData= ArrayList(10)
        wallet=intent.getParcelableExtra("data")
        zhujici=intent.getBooleanExtra("zhujici",false)
        wallet?.let {
            val mnemonic= wallet?.mnemonic;
            val mnemoniclist=mnemonic?.split(" ")
            onLayoutData(mnemoniclist);
        }
    }

    private fun onLayoutData(datas:List<String>?) {
        id_flowlayout.setAdapter(object : TagAdapter<String>(datas) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = mInflater.inflate(com.lang.progrom.R.layout.tv1, parent, false) as TextView
                tv.text = s
                return tv
            }
        })

        id_flowlayout.setMaxSelectCount(0)
        id_flowlayout.setOnTagClickListener(TagFlowLayout.OnTagClickListener { view, position, parent ->
            return@OnTagClickListener true
        })
    }


    fun onOkWallte(view: View) {
        val intent = Intent(this, RememberActivity::class.java)
        intent.putExtra("data",wallet)
        intent.putExtra("zhujici", zhujici)
        startActivity(intent)
    }


}