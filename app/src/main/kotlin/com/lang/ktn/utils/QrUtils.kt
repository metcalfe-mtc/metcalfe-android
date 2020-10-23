package com.lang.ktn.utils

import android.content.Context
import android.graphics.Color


import com.lang.progrom.R

import cn.bertsir.zbar.QrConfig
import cn.bertsir.zbar.QrManager

/**
 * @author : xiey
 * @project name : EAsset.
 * @package name  : com.bvc.easset.utils.
 * @date : 2018/6/26.
 * @signature : do my best.
 * @explain :
 */
object QrUtils {
    private fun qrConfig(context: Context): QrConfig {
        return QrConfig.Builder()
            .setDesText("")//Scan the text under the frame
            .setShowDes(false)//Displays the text below the scan box
            .setShowLight(true)//Display the flashlight button
            .setShowTitle(true)//show Title
            .setShowAlbum(true)//Displays the select button from the album
            .setCornerColor(context.resources.getColor(R.color.colorPrimary))//Set scan frame color
            .setLineColor(context.resources.getColor(R.color.colorPrimary))//Set the scan line color
            .setLineSpeed(QrConfig.LINE_MEDIUM)//Set the scan line speed
            .setScanType(QrConfig.TYPE_QRCODE)//Set the scan type (qr code, barcode, all, custom, default is qr code)
            .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE)//Set scan frame type (qr code or barcode, by default is qr code)
            .setCustombarcodeformat(QrConfig.BARCODE_I25)//This is only valid if the scan type is TYPE_CUSTOM
            .setPlaySound(true)//Whether to scan the bi~ sound successfully
            //                .setDingPath(R.raw.test)//Set prompt tone (not set as the default Ding~)
            .setIsOnlyCenter(false)//Whether only the contents in the box are recognized (the default is full-screen recognition)
            .setTitleText("")//set Tilte text
            .setTitleBackgroudColor(context.resources.getColor(android.R.color.transparent))//Set the status bar color
            .setTitleTextColor(Color.WHITE)//set Title text color
            .create()
    }

    fun getInstance(context: Context): QrManager {
        return QrManager.getInstance().init(qrConfig(context))
    }
}
