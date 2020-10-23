package com.lang.ktn.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.ui.home.HomeActivity
import com.lang.ktn.ui.main.MainActivity
import com.lang.ktn.ui.main.ZhuanChuActivity
import com.lang.progrom.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



//            var bean = SqlAddres()
//            bean.address = "dasdas"
//            bean.nickName = "第一款钱包"
//            bean.key = "key"
//            bean.zhujici = "vv"
//            accountTable.accountDao.insertUser(bean)


        launch(Dispatchers.IO) {
            // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            var accountTable = Room.databaseBuilder(application, AbstractUserDataBase::class.java, "userDataBase").build()
            var listAccount = accountTable.accountDao.queryAllUserInfo()
            withContext(Dispatchers.Main) {
                if (listAccount != null && listAccount.size > 0) {
                    val intent = Intent(application, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(application, HomeActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }

    }


}