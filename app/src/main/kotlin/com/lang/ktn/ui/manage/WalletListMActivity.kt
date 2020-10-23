package com.lang.ktn.ui.manage

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.ui.home.HomeActivity
import com.lang.ktn.ui.main.AssetAadpter
import com.lang.ktn.ui.main.MainActivity
import com.lang.ktn.ui.wallet.CreaterWallteActivity
import com.lang.ktn.ui.wallet.improt.ImportWalletActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_wallet_list.*
import kotlinx.android.synthetic.main.fragment_assets.*
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WalletListMActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_list)
        nav_tittle.setText(R.string.wallet_l_s_title)
        nav_img_left.setOnClickListener{ finish() }
//        nav_right.setText(getString(R.string.wallet_l_s_gl))
//        nav_right.visibility= View.VISIBLE


        val lay= LinearLayoutManager(this)
        lay.orientation= LinearLayoutManager.VERTICAL
        recycle_list_wallet.layoutManager=lay


//        nav_right.setOnClickListener {
//            val intent = Intent(it.context, WalletListActivity::class.java)
//            startActivity(intent)
//        }
        if(! EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initData()

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String?) {
        if("event1"==event){
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);
    }

    private fun initData(){
        launch(Dispatchers.IO) {
            var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
            var listAccount = accountTable.accountDao.queryAllUserInfo()
            withContext(Dispatchers.Main) {
                listAccount?.let {
                    var adapter=WalletMAadpter(it){
                        GlobalScope.launch(Dispatchers.IO)  {
                            listAccount?.let {
                                for (index in 0..listAccount.size - 1) {
                                    val cancalDault = listAccount.get(index)
                                    cancalDault.afault = "0"
                                    accountTable.accountDao.updateUser(cancalDault)
                                }
                            }
                            it.afault = "1"
                            accountTable.accountDao.updateUser(it)
                            withContext(Dispatchers.Main) {
                                EventBus.getDefault().post("event")
                                finish()
                            }
                        }
                    }
                    recycle_list_wallet.adapter= adapter
                }
            }
        }
    }

//    // 弹出框
//    fun showDialogSel(){
//        var dialog= Dialog(this,R.style.Dialog)
//        dialog.setContentView(R.layout.dialog_wallet_imcre)
//        dialog.show()
//        val windowManager = windowManager
//        val display = windowManager.defaultDisplay
//        val win = dialog.getWindow()
//        val lp = win!!.getAttributes()
//        val point = Point()
//        display.getSize(point)
//        val margin: Int=0
//        if (margin > -1) {
//            lp.width = point.x - margin * 2
//        } else {
//            lp.width = point.x
//        }
//
//        lp.gravity = Gravity.BOTTOM
//
//        win!!.setAttributes(lp)
//        win!!.setDimAmount(0.5f)
//        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener{
//            override fun onDismiss(dialog: DialogInterface){
//                win!!.setDimAmount(1f)
//            }
//        })
//        dialog.findViewById<TextView>(R.id.tv_choose1).setOnClickListener {
//            dialog.dismiss()
//            val intent = Intent(it.context, CreaterWallteActivity::class.java)
//            startActivity(intent)
//        }
//        dialog.findViewById<TextView>(R.id.tv_choose2).setOnClickListener {
//            dialog.dismiss()
//            val intent = Intent(it.context, ImportWalletActivity::class.java)
//            startActivity(intent)
//        }
//        dialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
//            dialog.dismiss()
//        }
//    }


}