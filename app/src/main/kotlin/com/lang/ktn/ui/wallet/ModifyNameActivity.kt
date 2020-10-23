package com.lang.ktn.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.room.Room
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.ui.manage.WalletMAadpter
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_modify_walletname.*
import kotlinx.android.synthetic.main.activity_wallet_list.*
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

class ModifyNameActivity: BaseActivity() {
    var sqladdres: SqlAddres?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_walletname)
        nav_tittle.setText(getString(R.string.my_wellet_name_title))
        nav_img_left.setOnClickListener{ finish() }

        sqladdres=intent.getParcelableExtra("data")
        sqladdres?.let {
            txt_mody_name.setText(it.nickName)
            it.nickName?.length?.let { it1 -> txt_mody_name.setSelection(it1) }
        }
    }

    fun onCreateWallte(view: View) {
        var infoName=txt_mody_name.text.toString().toString();
        if(!TextUtils.isEmpty(infoName)){
            sqladdres.let {
                sqladdres?.nickName=infoName;
            }
            launch(Dispatchers.IO) {
                var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
                 sqladdres?.let {
                     accountTable.accountDao.updateUser(it)
                     withContext(Dispatchers.Main) {
                         EventBus.getDefault().post("event")
                         EventBus.getDefault().post("event1")
                         EventBus.getDefault().post("event2")
                         EventBus.getDefault().post("event3")
                         finish()
                     }
                 }

            }
        }
//        val intent = Intent(this, BackupsActivity::class.java)
//        startActivity(intent)
    }


}