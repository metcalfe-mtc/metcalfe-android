package com.lang.ktn.ui.main

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.listener.OnDownloadListener
import com.azhon.appupdate.manager.DownloadManager
import com.flyco.tablayout.listener.OnTabSelectListener
import com.kaopiz.kprogresshud.KProgressHUD
import com.lang.ktn.base.BaseFragment
import com.lang.ktn.bean.resp.Version
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.ui.manage.WalletListActivity
import com.lang.ktn.ui.setting.AboutUsActivity
import com.lang.ktn.ui.wallet.improt.ImportWalletActivity
import com.lang.ktn.ui.web.WebViewActivity
import com.lang.ktn.utils.Constant
import com.lang.ktn.utils.SharedPref
import com.lang.ktn.utils.toast
import com.lang.progrom.BuildConfig
import com.lang.progrom.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_import_walle.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_mine.txt_info
import kotlinx.android.synthetic.main.fragment_zhujici.*
import kotlinx.android.synthetic.main.nav_layout.*
import java.io.File
import java.lang.Exception

// zichan
class MineFragment : BaseFragment() {
     lateinit var sharedPref: SharedPref
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav_img_left.visibility = View.GONE
        sharedPref = SharedPref(activity?.application)

        mine_txt_version.setText("v"+BuildConfig.VERSION_NAME)

        layout_yuyan.setOnClickListener {
//            val intent = Intent(it!!.context, AboutUsActivity::class.java)
//            startActivity(intent)

            val intent = Intent(it!!.context, WebViewActivity::class.java)
            intent.putExtra("url",BuildConfig.BASE_URL+"/api/custom/basic/redirect_aboutUs?lang="+(sharedPref.getData(Constant.LANGUAGE) as String))
            startActivity(intent)
        }

        layout_wallet_guanli.setOnClickListener {
            val intent = Intent(it!!.context, WalletListActivity::class.java)
            startActivity(intent)
        }

        layout_yuyan_fyab.setOnClickListener {
            showDialogSel()
        }
        layout_fuwuxieyi.setOnClickListener {
            val intent = Intent(it!!.context, WebViewActivity::class.java)
            intent.putExtra("url", BuildConfig.BASE_URL+"/api/custom/basic/redirect_serviceAgreement?lang="+(sharedPref.getData(Constant.LANGUAGE) as String))
            startActivity(intent)
        }

        layout_faq.setOnClickListener {
            val intent = Intent(it!!.context, WebViewActivity::class.java)
            intent.putExtra("url",BuildConfig.BASE_URL+"/api/custom/basic/redirect_faq?lang="+(sharedPref.getData(Constant.LANGUAGE) as String))
            startActivity(intent)
        }

        getVersion()
        layout_versionX.setOnClickListener {
            getVersionX()
        }
    }
    private fun getVersionX() {
        var dalogX = KProgressHUD.create(activity)
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
                toast(msg)
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
            toast(getString(R.string.about_new_info))
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

        val manager = DownloadManager.getInstance(activity)
        manager.setApkName("feniq.apk")
            .setApkUrl(bean.url)
            .setSmallIcon(R.mipmap.ic_launcher_op)
            .setShowNewerToast(true)
            .setConfiguration(configuration)
            .setApkVersionCode(BuildConfig.VERSION_CODE + 1)
            .setAuthorities(activity?.packageName)
            .setApkDescription(bean.desc)
            .download()
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
                            txt_info.visibility=View.VISIBLE
                        }else{
                            txt_info.setText(R.string.about_new_info)
                            txt_info.setTextColor(Color.GRAY)
                            txt_info.setBackgroundColor(Color.WHITE)
                            txt_info.visibility=View.VISIBLE
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

    //            showDialogSel()
//            showDialogSelPsd()
//    val intent = Intent(it.context, WalletListActivity::class.java)
//    startActivity(intent)
//
    //  语言选择 弹出框
    fun showDialogSel() {
        val languageChoose = sharedPref.getData(Constant.LANGUAGE) as String
        var dialog = Dialog(activity, R.style.Dialog)
        dialog.setContentView(R.layout.dialog_language2)
        dialog.show()

        dialog.findViewById<TextView>(R.id.tv_choose1).setOnClickListener {

            val language = Constant.ZH
            if (languageChoose == language) {
                dialog.dismiss()
                return@setOnClickListener
            }
            sharedPref.saveData(Constant.LANGUAGE, Constant.ZH)
            val intent = Intent(it?.getContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            dialog.dismiss()

        }
        dialog.findViewById<TextView>(R.id.tv_choose2).setOnClickListener {

            val language = Constant.EN
            if (languageChoose == language) {
                dialog.dismiss()
                return@setOnClickListener
            }
            sharedPref.saveData(Constant.LANGUAGE, Constant.EN)
            val intent = Intent(it?.getContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            dialog.dismiss()

        }
        dialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dialog.dismiss()
        }


        val windowManager = (this.context as Activity).windowManager
        val display = windowManager.defaultDisplay
        val win = dialog.getWindow()
        val lp = win!!.getAttributes()
        val point = Point()
        display.getSize(point)
        val margin: Int = 0
        if (margin > -1) {
            lp.width = point.x - margin * 2
        } else {
            lp.width = point.x
        }

        lp.gravity = Gravity.BOTTOM

        win!!.setAttributes(lp)
        win!!.setDimAmount(0.5f)
        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(dialog: DialogInterface) {
                win!!.setDimAmount(1f)
            }
        })
    }

    // 密码 弹出框
    @kotlin.Deprecated(
        message = "This class is no longer supported, do not use it.",
        level = DeprecationLevel.WARNING
    )
    fun showDialogSelPsd(onPsd: (psd: String, dialog: Dialog) -> Unit) {
        var dialog = Dialog(activity, R.style.Dialog)
        dialog.setContentView(R.layout.dialog_layout_pwd2)
        dialog.show()
        var edtPsdView = dialog.findViewById<EditText>(R.id.et_password);
        var txt_infoView = dialog.findViewById<TextView>(R.id.txt_info);

        dialog.findViewById<TextView>(R.id.tv_forget).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.confirm).setOnClickListener {

        }

        val windowManager = (this.context as Activity).windowManager
        val display = windowManager.defaultDisplay
        val win = dialog.getWindow()
        val lp = win!!.getAttributes()
        val point = Point()
        display.getSize(point)
        val margin: Int = 0
        if (margin > -1) {
            lp.width = point.x - margin * 2
        } else {
            lp.width = point.x
        }

        lp.gravity = Gravity.BOTTOM

        win!!.setAttributes(lp)
        win!!.setDimAmount(0.5f)
        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(dialog: DialogInterface) {
                win!!.setDimAmount(1f)
            }
        })
    }


}