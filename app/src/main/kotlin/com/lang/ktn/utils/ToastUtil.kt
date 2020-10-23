package com.lang.ktn.utils

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.lang.progrom.R

/**
 * —。—
 */
class ToastUtil {

    fun cancelToast() {
        CustomToast.INSTANCE.cancelToast()//销毁页面时，取消掉toast
        //        if (mToast != null) {
        //            mToast.cancel();
        //            mToast = null;
        //        }
    }

    enum class CustomToast {
        INSTANCE;

        // 实现单例
        private var mToast: Toast? = null
        private var mTvToast: TextView? = null
        fun showToast(ctx: Context, content: String) {
            if (mToast == null) {
                mToast = Toast(ctx)
                // (int unit, float value,
                //                                       DisplayMetrics metrics
                val yOffset= android.util.TypedValue.applyDimension(COMPLEX_UNIT_DIP,56f,ctx.resources.displayMetrics)
                mToast!!.setGravity(Gravity.CENTER, 0, -yOffset.toInt())//设置toast显示的位置，这是居中
                mToast!!.duration = Toast.LENGTH_SHORT//设置toast显示的时长
                val _root =
                    LayoutInflater.from(ctx).inflate(R.layout.layout_toast, null)//自定义样式，自定义布局文件
                mTvToast = _root.findViewById<View>(R.id.txt_toast) as TextView
                mToast!!.view = _root//设置自定义的view
            }
            mTvToast!!.text = content//设置文本
            mToast!!.show()//展示toast
        }

        fun showToast(ctx: Context, stringId: Int) {
            showToast(ctx, ctx.getString(stringId))
        }

        fun cancelToast() {
            if (mToast != null) {
                mToast!!.cancel()
                mToast = null
                mTvToast = null
            }
        }
    }

    companion object {

        //    private static Toast mToast;
        /**
         * 弹出Toast，保持单例，   潜在的问题：android有版本禁用通知权限之后，Toast无法弹出，这里统一处理。
         * 暂时没考虑好全新解决方案，待定，考虑SnakeBar处理。
         * @param ctx
         * @param message
         */

        fun show(ctx: Context, message: String) {
            //        if (mToast == null) {
            //            mToast = Toast.makeText(ctx,message,Toast.LENGTH_LONG);
            //        }
            //        mToast.setText(message);//设置文本
            //        mToast.show();//展示toast
            CustomToast.INSTANCE.cancelToast()
            if (!TextUtils.isEmpty(message)) { // 防止黑乎乎的Toast弹出
                CustomToast.INSTANCE.showToast(ctx, message)
            }
        }
    }


}
