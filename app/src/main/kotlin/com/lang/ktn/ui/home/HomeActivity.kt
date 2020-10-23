package com.lang.ktn.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lang.ktn.base.BaseActivity
import com.lang.ktn.ui.main.MainActivity
import com.lang.ktn.ui.wallet.CreaterWallteActivity
import com.lang.ktn.ui.wallet.improt.ImportWalletActivity
import com.lang.progrom.R
import kotlinx.android.synthetic.main.nav_layout.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun onCreateWallte(view: View) {
        val intent = Intent(this, CreaterWallteActivity::class.java)
        startActivity(intent)

    }

    fun onImPortWallte(view: View) {
        val intent = Intent(this, ImportWalletActivity::class.java)
        startActivity(intent)
    }


}