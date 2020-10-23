package com.lang.ktn.net.websocket

import com.google.gson.Gson
import com.lang.ktn.bean.PartBean
import com.lang.ktn.bean.resp.HomeAsset
import com.lang.ktn.bean.respws.marketWs
import com.lang.ktn.bean.sql.SqlAddres
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketState
import java.util.*

class WebSocketManager {
    //https://www.jianshu.com/p/5797b3d0ebd0
    // https://www.jianshu.com/p/27eb533b29f7

    //    https://blog.csdn.net/lckj686/article/details/80448471
    companion object WebSerClient {
        private val CONNECT_TIMEOUT = 10000 // 300000
        private val FRAME_QUEUE_SIZE = 5 // 300000
        val wsManager = WebSocketManager()
        val mapList = mutableMapOf<String, OnMsgBack>()
        var pairCurr = ""
        var sqladdres: SqlAddres=SqlAddres()
        lateinit var currentPart: PartBean
        var linesAsset: ArrayList<HomeAsset>?=null
        var webSocket: WebSocket? = null
    }




    fun connectWs() {
       if(webSocket!=null){
           if(webSocket?.state==WebSocketState.OPEN){ //connecting
               var marketWs= marketWs()
               marketWs.id= UUID.randomUUID().toString()
               marketWs.currency=currentPart.currency
               marketWs.issuer=currentPart.issuer
               marketWs.baseCurrency=currentPart.baseCurrency
               marketWs.baseIssuer=currentPart.baseIssuer
               webSocket?.sendText(Gson().toJson(marketWs))
               return@connectWs
           }
       }
//        webSocket = WebSocketFactory().setVerifyHostname(false).createSocket(BuildConfig.BASE_URL_WS, CONNECT_TIMEOUT)
//            .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
//            .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
//            .addListener(WsListener())//添加回调监听
//            .connectAsynchronously()//异步连接
    }

    fun sendMsg(any: Any) {
        webSocket?.let {
            if (it.state == WebSocketState.CONNECTING) {
                it.sendText(Gson().toJson(any))
            } else {
                it.recreate()
                it.connect()
            }
        }
    }


}