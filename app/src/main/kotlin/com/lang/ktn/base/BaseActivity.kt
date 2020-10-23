package com.lang.ktn.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lang.ktn.utils.*
import com.lang.progrom.R
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity() , CoroutineScope {
    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        this.getWindow().setBackgroundDrawableResource(R.color.color_ebeef5)
    }

     override fun attachBaseContext(base: Context) {
        val sharedPref = SharedPref(base)
        val yuyan = yuYan(base)
        sharedPref.saveData(Constant.LANGUAGE, yuyan)
        super.attachBaseContext(LocaleHelper.setLocale(base, yuyan))
        changeRefreshLanguage()
    }

    private fun changeRefreshLanguage() {
//        ClassicsHeader.REFRESH_HEADER_PULLDOWN = getString(R.string.header_pulldown)
        ClassicsHeader.REFRESH_HEADER_REFRESHING = getString(R.string.header_refreshing)
        ClassicsHeader.REFRESH_HEADER_LOADING = getString(R.string.header_loading)
        ClassicsHeader.REFRESH_HEADER_RELEASE = getString(R.string.header_release)
        ClassicsHeader.REFRESH_HEADER_FINISH = getString(R.string.header_finish)
        ClassicsHeader.REFRESH_HEADER_FAILED = getString(R.string.header_failed)
//        ClassicsHeader.REFRESH_HEADER_LASTTIME = getString(R.string.header_lasttime)

//        ClassicsFooter.REFRESH_FOOTER_PULLUP = getString(R.string.footer_pullup)
        ClassicsFooter.REFRESH_FOOTER_RELEASE = getString(R.string.footer_release)
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = getString(R.string.footer_refreshing)
        ClassicsFooter.REFRESH_FOOTER_LOADING = getString(R.string.footer_loading)
        ClassicsFooter.REFRESH_FOOTER_FINISH = getString(R.string.footer_finish)
        ClassicsFooter.REFRESH_FOOTER_FAILED = getString(R.string.footer_failed)
//        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = getString(R.string.footer_allloaded)
    }

    private fun yuYan(base: Context): String {
        val sharedPref = SharedPref(base)
        val lay = sharedPref.getData(Constant.LANGUAGE)
        if(lay==null){
            val locale = Locale.getDefault().language
            return if (!TextUtils.isEmpty(locale)) {
                if (Constant.ZH.equals(locale)) {
                    "zh"
                } else if (Constant.EN.equals(locale)) {
                    "en"
                } else{
                    "en"
                }
            } else {
                "en"
            }
        }else{
            if (Constant.ZH.equals(lay)) {
                return "zh"
            } else if (Constant.EN.equals(lay)) {
                return "en"
            }
        }
        return "en"
    }


    override fun onDestroy() {
        super.onDestroy()
        // 关闭页面后，结束所有协程任务
        job.cancel()
    }

    fun makeText(msg:String){
        ToastUtil.show(this.application,msg)
    }
    fun getActivity():Activity{
        return this
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}