package com.lang.ktn.ui.manage

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import cn.bertsir.zbar.QRUtils
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.utils.FileUtils
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_wallet_addres.*
import kotlinx.android.synthetic.main.nav_layout.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class WalletKeyActivity: BaseActivity() {
    var sqladdres: SqlAddres? = null
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_key)
        nav_tittle.setText(getString(R.string.wallet_key_title))
        nav_img_left.setOnClickListener{ finish() }


        sqladdres = intent.getParcelableExtra("data")
        bitmap= QRUtils.getInstance().createQRCode(sqladdres?.secret)
        img_addres.setImageBitmap(bitmap)
        img_addres_txt.setText(sqladdres?.secret)

    }

    fun onCopy(view: View) {
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText(sqladdres?.secret, sqladdres?.secret)
        cm.setPrimaryClip(mClipData)
        makeText(getString(R.string.zichan_detail_copy_success))
    }
    fun onSave(view: View) {
        methodRequiresTwoPermission()
    }

    public val RC_CAMERA_PERM = 123
    @AfterPermissionGranted(value=123)
    fun methodRequiresTwoPermission() {
        val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            bitmap?.let {
                val path= Environment.getExternalStorageDirectory().absolutePath+ "/mtcwallet/";
                sqladdres?.address?.let { it1 -> FileUtils.saveBitmap(application,it, it1,path) }
                makeText(getString(R.string.wallet_pic_save))
            }
        } else {
            EasyPermissions.requestPermissions(this,getString(R.string.wallet_pic_tips_info), RC_CAMERA_PERM, *perms)
        }
    }


}