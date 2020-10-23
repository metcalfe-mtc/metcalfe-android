package com.lang.ktn.ui.setting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.listener.OnDownloadListener
import com.azhon.appupdate.manager.DownloadManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.resp.Version
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.ui.web.WebViewActivity
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.SharedPref
import com.lang.progrom.BuildConfig
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.nav_layout.*
import java.io.File
import java.lang.Exception

class AboutUsActivity: BaseActivity() {
    lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPref = SharedPref(application)

        nav_tittle.setText(getString(R.string.about_us))
        nav_img_left.setOnClickListener{ finish() }
        init()
        about_version.setText(BuildConfig.VERSION_NAME)
        getVersion()
    }

    private fun getVersion() {
        var mapmu= mutableMapOf<String,String>()
        mapmu["code"]=BuildConfig.VERSION_CODE.toString()
        mapmu["type"]="android"
        retrofit<Version> {
            api = Api.instance.service.getVersion(mapmu)
            onSuccess{ bean: Version?, code: String, msg: String->

                bean?.let {
                    try {

                        if(bean.upgrade){
                            txt_info.setText("new")
                            txt_info.setTextColor(Color.WHITE)
                            txt_info.setBackgroundResource(R.drawable.shape_red_bg)
                        }else{
                            txt_info.setText(R.string.about_new_info)
                            txt_info.setTextColor(Color.GRAY)
                            txt_info.setBackgroundColor(Color.WHITE)
                        }
                    }catch (E: Exception){}
                }
            }
            onFail{ t: Version?, code: String, msg: String->
//                dalogX.dismiss()
//                makeText(msg)
            }
        }
    }


    private fun checkInfo(bean: Version) {
        /*
         * 整个库允许配置的内容
         * 非必选
         */
        var isMust = false
        if(!bean.upgrade){
            makeText(getString(R.string.about_new_info))
            return
        }else{
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

                }

                override fun downloading(max: Int, progress: Int) {

                }

                override fun done(apk: File) {

                }

                override fun cancel() {

                }

                override fun error(e: Exception) {

                }
            })

        val manager = DownloadManager.getInstance(this)
        manager.setApkName("feniq.apk")
            .setApkUrl(bean.url)
            .setSmallIcon(R.mipmap.ic_launcher_op)
            .setShowNewerToast(true)
            .setConfiguration(configuration)
            .setApkVersionCode(BuildConfig.VERSION_CODE + 1)
            .setAuthorities(packageName)
            .setApkDescription(bean.desc)
            .download()
    }


    // /api/custom/basic/redirect_aboutUs?lang=cn
//    /api/custom/basic/redirect_faq?lang=cn
//    /api/custom/basic/redirect_serviceAgreement?lang=cn

    private fun init() {
        layout_about.setOnClickListener {
            val intent = Intent(it!!.context, WebViewActivity::class.java)
            intent.putExtra("url",BuildConfig.BASE_URL+"/api/custom/basic/redirect_aboutUs?lang="+(sharedPref.getData(Constant.LANGUAGE) as String))
            startActivity(intent)
        }
        layout_fuwutiao.setOnClickListener {
            val intent = Intent(it!!.context, WebViewActivity::class.java)
            intent.putExtra("url",BuildConfig.BASE_URL+"/api/custom/basic/redirect_serviceAgreement?lang="+(sharedPref.getData(Constant.LANGUAGE) as String))
            startActivity(intent)
        }
        layout_qrc.setOnClickListener {
            val intent = Intent(it!!.context, WebViewActivity::class.java)
            intent.putExtra("url",BuildConfig.BASE_URL+"/api/custom/basic/redirect_faq?lang="+(sharedPref.getData(Constant.LANGUAGE) as String))
            startActivity(intent)
        }
        layout_version.setOnClickListener {
            getVersionX()
        }
    }


    private fun getVersionX() {
        var dalogX = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2).setDimAmount(0.5f)
        dalogX.show()
        var mapmu= mutableMapOf<String,String>()
        mapmu["code"]=BuildConfig.VERSION_CODE.toString()
        mapmu["type"]="android"

        retrofit<Version> {
            api = Api.instance.service.getVersion(mapmu)
            onSuccess{ bean: Version?, code: String, msg: String->
                dalogX.dismiss()
                bean?.let {
                    try {
                        checkInfo(it)
                    }catch (E: Exception){}
                }
            }
            onFail{ t: Version?, code: String, msg: String->
                dalogX.dismiss()
                makeText(msg)
            }
        }
    }



}