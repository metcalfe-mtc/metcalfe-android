package com.lang.ktn.ui.manage

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.bertsir.zbar.QRUtils
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.ui.wallet.shouxin.WalletRecodeActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.activity_wallet_addres.*
import kotlinx.android.synthetic.main.nav_layout.*
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.os.Environment
import android.text.TextUtils
import android.widget.TextView
import com.lang.ktn.ui.showDialogSelPsd
import com.lang.ktn.utils.AesEBC
import com.lang.ktn.utils.FileUtils
import com.lang.ktn.utils.QrUtils
import kotlinx.android.synthetic.main.activity_assets_zhuancuh.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception


class WalletAddressActivity : BaseActivity() {
    var sqladdres: SqlAddres? = null
    var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_addres)
        nav_tittle.setText(getString(R.string.wallet_adss_title))
        nav_img_left.setOnClickListener{ finish() }

        nav_right.setText(getString(R.string.wallet_adss_key))
        nav_right.visibility = View.VISIBLE
        nav_right.setOnClickListener {

            showDialogSelPsd(this) { psd: String, dialog: Dialog, txt_infoView: TextView ->
                var key:String?=null
                try {
                   sqladdres?.key?.let {
                       key= AesEBC.ecrypt(psd, it)
                    }
                }catch (ex:Exception){
                    txt_infoView.setText(getString(R.string.dialog_psd_error))
                    return@showDialogSelPsd
                }
                dialog.dismiss()
                sqladdres?.secret=key;
                val intent = Intent(it.context, WalletKeyActivity::class.java)
                intent.putExtra("data", sqladdres)
                startActivity(intent)
            }
        }
        sqladdres = intent.getParcelableExtra("data")
        bitmap = QRUtils.getInstance().createQRCode(sqladdres?.address)
        img_addres.setImageBitmap(bitmap)
        img_addres_txt.setText(sqladdres?.address)

    }

    fun onCopy(view: View) {
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText(sqladdres?.address, sqladdres?.address)
        cm.setPrimaryClip(mClipData)
        makeText(getString(R.string.zichan_detail_copy_success))
    }

    fun onSave(view: View) {
        methodRequiresTwoPermission()
    }

    public val RC_CAMERA_PERM = 123
    @AfterPermissionGranted(value = 123)
    fun methodRequiresTwoPermission() {
        val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            bitmap?.let {
                val path = Environment.getExternalStorageDirectory().absolutePath + "/mtcwallet/";
                sqladdres?.address?.let { it1 -> FileUtils.saveBitmap(application, it, it1, path) }
                makeText(getString(R.string.wallet_pic_save))
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.wallet_pic_tips_info), RC_CAMERA_PERM, *perms)
        }
    }

}