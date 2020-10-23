package com.lang.ktn.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.makeText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.kaopiz.kprogresshud.KProgressHUD
import com.lang.ktn.base.BaseAppliton
import com.lang.ktn.base.BaseFragment
import com.lang.ktn.bean.resp.AccountLines
import com.lang.ktn.bean.resp.HomeAsset
import com.lang.ktn.bean.sql.AbstractUserDataBase
import com.lang.ktn.bean.sql.SqlAddres
import com.lang.ktn.net.Api
import com.lang.ktn.net.exc.retrofit
import com.lang.ktn.net.exc.retrofitApi
import com.lang.ktn.net.websocket.WebSocketManager
import com.lang.ktn.ui.home.HomeActivity
import com.lang.ktn.ui.manage.WalletAddressActivity
import com.lang.ktn.ui.manage.WalletListActivity
import com.lang.ktn.ui.manage.WalletListMActivity
import com.lang.ktn.ui.wallet.shouxin.WalletAssetActivity
import com.lang.ktn.utils.toast
import com.lang.progrom.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_assets.*
import kotlinx.android.synthetic.main.nav_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import java.util.ArrayList



// zichan
class AssetsFragment:BaseFragment() {

    var  sqladdres: SqlAddres?=null
    var  weekForecast: ArrayList<HomeAsset> =ArrayList()
    val zhuZichan=HomeAsset();
    var lines: ArrayList<HomeAsset>?=null
    lateinit var dalogi: KProgressHUD
    var isJiHuo=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(! EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        return inflater.inflate(R.layout.fragment_assets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav_img_left.visibility=View.GONE
        nav_tittle.setText(getString(R.string.main_table_zichan))

        dalogi = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2).setDimAmount(0.5f)

        nav_img_right.visibility= View.VISIBLE
        nav_img_right.setImageResource(R.mipmap.liebiao)

        val lay= LinearLayoutManager(activity)
        lay.orientation= LinearLayoutManager.VERTICAL
        recycle_jil.layoutManager=lay
        recycle_jil.adapter=AssetAadpter(weekForecast){

            if(!isJiHuo){
                toast(getString(R.string.wallet_no_action))
                return@AssetAadpter
            }
            val intent = Intent(view.context, AssetListActivity::class.java)
            intent.putExtra("data",it)
            intent.putExtra("sqladdres",sqladdres)
            intent.putExtra("currency",sqladdres!!.address)
            startActivity(intent)
        }

        wallet_save.setOnClickListener {//授信
//            val intent = Intent(it.context, WalletAssetActivity::class.java)
//            startActivity(intent)
            if(!isJiHuo){
                toast(getString(R.string.wallet_no_action))
                return@setOnClickListener
            }

            sqladdres?.let {
                val intent = Intent(activity, WalletAssetActivity::class.java)
                intent.putExtra("data", sqladdres)
                intent.putExtra("lines", lines)
                startActivity(intent)
            }
        }
//        layout_addres.setOnClickListener {//当前钱包的地址
        layout_gou_qianbao.setOnClickListener {//当前钱包的地址
            sqladdres?.let {
                val intent = Intent(activity, WalletAddressActivity::class.java)
                intent.putExtra("data", sqladdres)
                startActivity(intent)
            }
        }

        nav_img_right.setOnClickListener {
//            val intent = Intent(it.context, WalletListActivity::class.java)
            val intent = Intent(it.context, WalletListMActivity::class.java)
            startActivity(intent)
        }
        initData(true)

        refreshLayout.setOnRefreshListener {
            initData(false)
        }
        refreshLayout.setEnableLoadMore(false)
//        refreshLayout.setOnRefreshListener(object : OnRefreshListener() {
//            fun onRefresh(refreshlayout: RefreshLayout) {
//                refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
//            }
//        })

    }

    private fun accountInfo(map: Map<String, String>) {
        retrofit<HomeAsset> {
            api = Api.instance.service.accountInfo(map)
            onSuccess{ bean:HomeAsset?,code: String, msg: String->
                bean?.let {
                    isJiHuo=true
                    var data1=weekForecast[0];
                    it.decimals?.let {
                        data1.balance=bean.balance?.toLong()!!.div(Math.pow(10.0,it.toDouble())).toString()

                        data1.reserveBase=bean.reserveBase
                        data1.ownerCount=bean.ownerCount
                        data1.reserveInc=bean.reserveInc
                        data1.decimals=bean.decimals
                        recycle_jil.adapter?.notifyDataSetChanged()

                    }
                }
            }
            onFail{ t:HomeAsset?,code: String, msg: String->
                if("actNotFound"==code){
                    isJiHuo=false
                    toast(getString(R.string.wallet_no_action))
                }
            }
        }
    }


    private fun accountList(map: Map<String, String>) {
        lines=null
        retrofit<AccountLines> {
            api = Api.instance.service.accountLines(map)
            onSuccess{ bean:AccountLines?,code: String, msg: String->

                if(dalogi!=null && dalogi.isShowing ) {
                    dalogi.dismiss()
                }

                bean?.lines?.let {
                    isJiHuo=true
                    weekForecast.clear();
                    weekForecast.add(zhuZichan)
                    lines=bean.lines
                    weekForecast.addAll(bean.lines)
                    com.lang.ktn.net.websocket.WebSocketManager.linesAsset=weekForecast
                    recycle_jil.adapter?.notifyDataSetChanged()
                }
                if(refreshLayout!=null){
                    refreshLayout.finishRefresh();
                }
            }
            onFail{ t:AccountLines?,code: String, msg: String->
                if("actNotFound"==code){
                    isJiHuo=false
                }
                if(dalogi!=null && dalogi.isShowing ) {
                    dalogi.dismiss()
                }
                if(refreshLayout!=null){
                    refreshLayout.finishRefresh();
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String?) {
        if("event"==event){
            initData(true)
        }
    }







    private fun initData(isShowDlalig: Boolean) {

        if(isShowDlalig) {
           if(dalogi!=null) {
               dalogi.show()
           }
        }
        weekForecast.clear()
        isJiHuo=false
        launch(Dispatchers.IO) {
            // 在一个公共线程池中创建一个协程
            var accountTable = Room.databaseBuilder(activity!!.application, AbstractUserDataBase::class.java, "userDataBase").build()
            var listAccount = accountTable.accountDao.getDefaultWallet("1")
            var listAccount0 = accountTable.accountDao.getDefaultWallet("0")
            withContext(Dispatchers.Main) {
                if(dalogi!=null && dalogi.isShowing){
                    dalogi.dismiss()
                }
                if(listAccount!=null){
                    listAccount?.let {
                        sqladdres=it
                        WebSocketManager.sqladdres=it
                        txt_wallet_name.setText(it.nickName)
                        txt_wallet_addres.setText(it.address)
                    }
                }else{
                    listAccount0?.let {
                        sqladdres=it
                        WebSocketManager.sqladdres=it
                        txt_wallet_name.setText(it.nickName)
                        txt_wallet_addres.setText(it.address)
                    }
                }
                sqladdres?.address?.let {
                    zhuZichan.currency= com.lang.ktn.utils.Constant.CURRN
                    zhuZichan.balance="0"
                    zhuZichan.account=it
                    weekForecast.add(zhuZichan)
                    recycle_jil.adapter?.notifyDataSetChanged()
                    accountInfo(mapOf(Pair("account",it),Pair("withFee","true")));
                    accountList(mapOf(Pair("account",it)))
                }
            }
        }

    }

}