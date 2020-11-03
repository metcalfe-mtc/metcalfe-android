package com.lang.ktn.ui.main

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.listener.OnDownloadListener
import com.azhon.appupdate.manager.DownloadManager
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.TabEntity
import com.lang.ktn.bean.resp.Version
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.utils.toast
import com.lang.progrom.BuildConfig
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_main_sdrc.*
import java.io.File
import java.util.*


class MainActivity : BaseActivity() {

    private val mIconUnselectIds = intArrayOf(R.mipmap.icon_qianbao, R.mipmap.icon_mine_sel)

    private val mIconUnselectIdsx = intArrayOf(R.mipmap.icon_qianbao_un, R.mipmap.icon_mine)


    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val listTitle = ArrayList<String>()

    val fragments = ArrayList<Fragment>()


    lateinit var emailLoginFragment: Fragment

    //    lateinit var transactionfragment: Fragment
    lateinit var phoneLoginFragment: Fragment
    private val manager = DownloadManager.getInstance(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sdrc)

        listTitle.add(getString(R.string.main_table_zichan))
        listTitle.add(getString(R.string.main_table_mine))


        mTabEntities.add(
            TabEntity(
                listTitle.get(0),
                mIconUnselectIds.get(0),
                mIconUnselectIdsx.get(0)
            )
        )
        mTabEntities.add(
            TabEntity(
                listTitle.get(1),
                mIconUnselectIds.get(1),
                mIconUnselectIdsx.get(1)
            )
        )


        emailLoginFragment = AssetsFragment()
//         transactionfragment = TransactionFragment()
        phoneLoginFragment = MineFragment()


        fragments.add(emailLoginFragment)
//        fragments.add(transactionfragment)
        fragments.add(phoneLoginFragment)


//        if (!transactionfragment.isAdded) {
//            supportFragmentManager.beginTransaction().add(R.id.layFrame,transactionfragment).commitAllowingStateLoss()
//        }
        if (!phoneLoginFragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.layFrame, phoneLoginFragment)
                .commitAllowingStateLoss()
        }
        if (!emailLoginFragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.layFrame, emailLoginFragment)
                .commitAllowingStateLoss()
        }

        tab_login.setTabData(mTabEntities)

        tab_login.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                if (position == 0) {
                    supportFragmentManager.beginTransaction().hide(phoneLoginFragment)
                        .show(emailLoginFragment).commitAllowingStateLoss()
                } else if (position == 1) {
                    supportFragmentManager.beginTransaction().hide(emailLoginFragment)
                        .show(phoneLoginFragment).commitAllowingStateLoss()
                }
            }

            override fun onTabReselect(position: Int) {}
        })

        getVersion()
        supportFragmentManager.beginTransaction().hide(phoneLoginFragment)
            .show(emailLoginFragment).commitAllowingStateLoss()
    }


    private fun getVersion() {
        var mapmu = mutableMapOf<String, String>()
        mapmu["code"] = BuildConfig.VERSION_CODE.toString()
        mapmu["type"] = "android"

        retrofit<Version> {
            api = Api.instance.service.getVersion(mapmu)
            onSuccess { bean: Version?, code: String, msg: String ->
                bean?.let {
                    try {
                        checkInfo(it)
                    } catch (E: Exception) {
                    }
                }
            }
            onFail { t: Version?, code: String, msg: String ->

            }
        }
    }


    private fun checkInfo(bean: Version) {
        /*
         * 整个库允许配置的内容
         * 非必选
         */
        var isMust = false
        //        "updateflag":"0",//0-不需要更新，1-强制更新，2-不强制更新
//        "upgrade": false,
//        "force": true,
        if (!bean.upgrade) {
            return
        } else {
            isMust = bean.force
        }
        val configuration = UpdateConfiguration()
            //输出错误日志
            .setEnableLog(true)
            //设置自定义的下载
            //.setHttpManager()
            //下载完成自动跳动安装页面
            .setJumpInstallPage(true)
            //设置对话框背景图片 (图片规范参照demo中的示例图)
            .setDialogImage(R.drawable.ic_dialog_default1)
            //设置按钮的颜色
            .setDialogButtonColor(Color.parseColor("#33BBFF"))
            //设置按钮的文字颜色
            .setDialogButtonTextColor(Color.WHITE)
            //设置是否显示通知栏进度
            .setShowNotification(true)
            //设置是否提示后台下载toast
            .setShowBgdToast(true)
            //设置强制更新
            .setForcedUpgrade(isMust)
            //设置对话框按钮的点击监听
            .setButtonClickListener { }
            //设置下载过程的监听
            .setOnDownloadListener(object : OnDownloadListener {
                override fun start() {
                    Log.d("download_apk", "start")

                }

                override fun downloading(max: Int, progress: Int) {
                    Log.d("download_apk", "downloading" + progress)

                }

                override fun done(apk: File) {
                    Log.d("download_apk", "done")

                }

                override fun cancel() {
                    Log.d("download_apk", "cancel")
                }

                override fun error(e: Exception) {
                    Log.d("download_apk", e.message)
                    mHanlder.sendEmptyMessage(0)
                }
            })

        manager.setApkName("feniq.apk")
            .setApkUrl(bean.url)
            .setSmallIcon(R.drawable.app_icon)
            .setShowNewerToast(true)
            .setConfiguration(configuration)
            .setApkVersionCode(BuildConfig.VERSION_CODE + 1)
            .setAuthorities(packageName)
            .setApkDescription(bean.desc)
            .download()
    }


    private inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return fragments.size
        }

//        override fun getPageTitle(position: Int): CharSequence? {
//            return listTitle[position]
//        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "dage "
        }

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }
    }


    private val mHanlder: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 0) {
                toast(getString(R.string.app_update_msg))
                manager.defaultDialog.dismiss()
                manager.download()
            }
        }
    }

}