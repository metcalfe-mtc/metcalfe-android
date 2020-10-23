package com.lang.ktn.ui.wallet.zhujici

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lang.ktn.base.BaseActivity
import kotlinx.android.synthetic.main.nav_layout.*
import android.widget.TextView
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.FlowLayout
import kotlinx.android.synthetic.main.activity_wallet_backremember.*
import com.zhy.view.flowlayout.TagFlowLayout
import android.widget.Toast
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.room.Room
import com.lang.ktn.bean.WalletBean
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.ui.home.HomeActivity
import com.lang.ktn.ui.main.MainActivity
import com.lang.ktn.ui.manage.WalletActivity
import com.lang.ktn.utils.Constant
import com.lang.progrom.R
import com.zhy.view.flowlayout.TagView
import kotlinx.android.synthetic.main.activity_create_wallet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class RememberActivity : BaseActivity() {
    lateinit var mInflater: LayoutInflater;
    lateinit var onOriData: ArrayList<String>;
    var selectedSeries: MutableList<String>?=null;
    var mnemoniclist: List<String>? = null;
    var wallet: WalletBean? = null
    var zhujici: Boolean?=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.lang.progrom.R.layout.activity_wallet_backremember)
        nav_tittle.setText(getString(R.string.back_ac_ok2_title))
        nav_img_left.setOnClickListener{ finish() }


        mInflater = LayoutInflater.from(this);
        onOriData = ArrayList(10)
        wallet = intent.getParcelableExtra("data")
        zhujici=intent.getBooleanExtra("zhujici",false)
        wallet?.let {
            val mnemonic = wallet?.mnemonic;
            mnemoniclist = mnemonic?.split(" ")
//            val selectedSeries = mnemoniclist?.toMutableList()
            selectedSeries = mnemoniclist?.toMutableList()
            Collections.shuffle(selectedSeries)
            onLayoutData();
        }
    }


    fun TextView.cheanTextInfo(): Unit {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val oldPsd1 = edt_name.text.toString().trim()
                val oldPs2 = edt_psd1.text.toString().trim()
                val sercert = edt_psd2.text.toString().trim()
                btn_create_wallet.isEnabled = (!TextUtils.isEmpty(oldPsd1)) && oldPs2.length >= Constant.PSDSIZE && sercert.length>=Constant.PSDSIZE
            }
        })
    }


    private fun onLayoutData() {
        // 上面的View处理
        id_flowlayout_back.setAdapter(object : TagAdapter<String>(onOriData) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = mInflater.inflate(com.lang.progrom.R.layout.tv, parent, false) as TextView
                tv.text = s
                return tv
            }
        })
        id_flowlayout_back.setOnTagClickListener {
                view, position, parent ->

            onOriData?.let {
                it?.get(position)?.let {
                    selectedSeries?.add(it)
                }
                onOriData.removeAt(position)
                id_flowlayout_back.adapter.notifyDataChanged()
                id_flowlayout.adapter.notifyDataChanged()
            }
            return@setOnTagClickListener true
        }




        id_flowlayout.setAdapter(object : TagAdapter<String>(selectedSeries) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = mInflater.inflate(com.lang.progrom.R.layout.tv, parent, false) as TextView
                tv.text = s
                return tv
            }
        })

        id_flowlayout.setOnTagClickListener(TagFlowLayout.OnTagClickListener { view, position, parent ->
            if (view is TagView) {
                selectedSeries?.let {
                    it?.get(position)?.let {
                        onOriData.add(it)
                    }
                    it.removeAt(position)

                    id_flowlayout_back.adapter.notifyDataChanged()
                    id_flowlayout.adapter.notifyDataChanged()

                }

                for (key in 0..onOriData.size-1){
                  if(mnemoniclist?.get(key)!=onOriData[key]){
                      txt_show_error.visibility=View.VISIBLE
                      return@OnTagClickListener true
                  }else{
                      txt_show_error.visibility=View.INVISIBLE
                  }
                }
                if(onOriData.size==0){
                    txt_show_error.visibility=View.INVISIBLE
                }

            }
            return@OnTagClickListener true
        })
    }


    fun onOkWallte(view: View) {
        if(onOriData.size==0){
            makeText(getString(R.string.back_ac_ok2_info))
            return
        }
        if(txt_show_error.visibility==View.VISIBLE ){
            makeText(getString(R.string.back_ac_ok_worderro))
            return
        }
        if(selectedSeries!!.size>0){
            makeText(getString(R.string.back_ac_ok2_info))
            return
        }


        if( zhujici!=null && zhujici as Boolean){
            val intent = Intent(application, WalletActivity::class.java)
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            return
        }

        launch(Dispatchers.IO) {
           var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
           var listAccount = accountTable.accountDao.queryAllUserInfo()
            listAccount?.let {
                for (index in 0..listAccount.size-1){
                    val cancalDault=listAccount.get(index)
                    cancalDault.afault="0"
                    accountTable.accountDao.updateUser(cancalDault)
                }
            }

           val addres=SqlAddres()
            addres.address=wallet?.wallet?.address;
            addres.key=wallet?.wallet?.secret;
            addres.nickName=wallet?.nameWallet
            addres.afault="1"


             wallet?.wallet?.address?.let {
                 var  getWallet=accountTable.accountDao.getUserById(it)
                 if(getWallet!=null){
                     withContext(Dispatchers.Main) {
                         makeText(getString(R.string.back_ac_ok_wallet_have))
                     }
                 }else{
                     var insert = accountTable.accountDao.insertUser(addres)
                     withContext(Dispatchers.Main) {
                         val intent = Intent(application, MainActivity::class.java)
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)
                     }
                 }


            };


        }



    }


}