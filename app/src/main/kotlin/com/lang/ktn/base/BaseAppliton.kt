package com.lang.ktn.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.lang.ktn.utils.Loggers
import com.lang.progrom.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

class BaseAppliton :Application(){



    override fun onCreate() {
        super.onCreate()
        application=this
    }
    private var mFinalCount: Int = 0

    private fun registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
                mFinalCount++
                //如果mFinalCount ==1，说明是从后台到前台
                Log.e("onActivityStarted", mFinalCount.toString() + "")
                if (mFinalCount === 1) {
                    //说明从后台回到了前台
                    Loggers.e("说明从后台回到了前台")
                }
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
                mFinalCount--
                //如果mFinalCount ==0，说明是前台到后台
                Log.i("onActivityStopped", mFinalCount.toString() + "")
                if (mFinalCount === 0) {
                    //说明从前台回到了后台
                    Loggers.e("说明从前台回到了后台")
                }
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

            }
        })
    }


    companion object {
        lateinit var application:BaseAppliton
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
                ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }
}