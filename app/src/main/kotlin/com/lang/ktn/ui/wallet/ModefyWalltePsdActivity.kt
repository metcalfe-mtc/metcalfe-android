package com.lang.ktn.ui.wallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.room.Room
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.ui.wallet.zhujici.BackupsActivity
import com.lang.ktn.utils.AesEBC
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.changeEyes
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_modify_walletpsd.*
import kotlinx.android.synthetic.main.activity_modify_walletpsd.btn_mdy_pk
import kotlinx.android.synthetic.main.activity_modify_walletpsd.edt_newpsd_1
import kotlinx.android.synthetic.main.activity_modify_walletpsd.edt_newpsd_2
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ModefyWalltePsdActivity:BaseActivity() {
    var sqladdres: SqlAddres? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_walletpsd)
        sqladdres = intent.getParcelableExtra("data")
        nav_tittle.setText(getString(R.string.my_psd_title))
        nav_img_left.setOnClickListener{ finish() }

        edt_old_psd.onCreateWallte();
        edt_newpsd_1.onCreateWallte();
        edt_newpsd_2.onCreateWallte();

        lookPwd_old_1.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_old_psd,isChecked) }
        lookPwd_psd11.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_1,isChecked) }
        lookPwd_psd12.setOnCheckedChangeListener { buttonView, isChecked -> changeEyes(edt_newpsd_2,isChecked) }


    }

    fun onCreateWallte(view: View) {
        val oldPsd=edt_old_psd.text.toString().trim()
        val oldPsd1=edt_newpsd_1.text.toString().trim()
        val oldPs2=edt_newpsd_2.text.toString().trim()
        if (oldPsd1!=oldPs2){
            makeText(getString(R.string.my_psd_no_some))
            return
        }


        launch(Dispatchers.IO) {
            var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
            sqladdres?.id?.let {
                sqladdres = accountTable.accountDao.getUserById(it)
                sqladdres?.let {
                    withContext(Dispatchers.Main) {
                        sqladdres?.let {
                            var  key:String?=null
                            sqladdres?.let {
                                try {
                                    sqladdres?.key?.let {
                                        key= AesEBC.ecrypt(oldPsd, it)
                                    }
                                }catch (ex: Exception){
                                    makeText(getString(R.string.dialog_psd_error))
                                    return@let
                                }
                                try {
                                    key=AesEBC.encrypt(oldPsd1, key!!)
                                    sqladdres?.key=key;
                                    launch(Dispatchers.IO) {
                                        accountTable.accountDao.updateUser(sqladdres!!)
                                        withContext(Dispatchers.Main) {
                                           finish()
                                        }
                                    }
                                }catch (E:Exception){
                                    makeText(getString(R.string.js_error_info))
                                    return@let
                                }
                            }

                        }
                    }
                }
            }


        }



    }



    fun  TextView.onCreateWallte():Unit{
        this.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val oldPsd=edt_old_psd.text.toString().trim()
                val oldPsd1=edt_newpsd_1.text.toString().trim()
                val oldPs2=edt_newpsd_2.text.toString().trim()
                btn_mdy_pk.isEnabled = oldPsd.length>= Constant.PSDSIZE  && oldPsd1.length>=Constant.PSDSIZE  && oldPs2.length>=Constant.PSDSIZE
            }
        })
    }





}