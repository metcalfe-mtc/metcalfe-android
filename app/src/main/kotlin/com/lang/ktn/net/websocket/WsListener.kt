package com.lang.ktn.net.websocket

import android.util.Log
import com.google.gson.Gson
import com.lang.ktn.bean.respws.marketWs
import com.neovisionaries.ws.client.*
import java.util.*

class WsListener : WebSocketListener {
    override fun handleCallbackError(websocket: WebSocket?, cause: Throwable?) {
    }

    override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
    }

    override fun onThreadCreated(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
    }

    override fun onThreadStarted(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
    }

    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState?) {
    }

    override fun onTextMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        data: ByteArray?
    ) {
    }

    override fun onTextFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
    }

    override fun onUnexpectedError(websocket: WebSocket?, cause: WebSocketException?) {
    }

    override fun onConnectError(websocket: WebSocket?, cause: WebSocketException?) {
    }

    override fun onSendError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?
    ) {

    }

    override fun onFrameUnsent(websocket: WebSocket?, frame: WebSocketFrame?) {
    }

    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) {
    }

    override fun onSendingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onBinaryFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
    }

    override fun onPingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onSendingHandshake(
        websocket: WebSocket?,
        requestLine: String?,
        headers: MutableList<Array<String>>?
    ) {
    }

    override fun onTextMessage(websocket: WebSocket?, text: String?) {
        text?.let {
            WebSocketManager.mapList.forEach{
                it.value.onText(text)
                Log.e("onTextMessage", "websocket: $websocket")
                Log.e("onTextMessage", "onTextMessage: $text")
            }
        }
    }

    override fun onTextMessage(websocket: WebSocket?, data: ByteArray?) {
    }

    override fun onFrameError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?
    ) {
    }

    override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
    }

    override fun onBinaryMessage(websocket: WebSocket?, binary: ByteArray?) {
    }

    override fun onContinuationFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onConnected(
        websocket: WebSocket?,
        headers: MutableMap<String, MutableList<String>>?
    ) {
        websocket?.let {
            if(WebSocketManager.currentPart!=null){
                var marketWs= marketWs()
                marketWs.id=UUID.randomUUID().toString()
                marketWs.currency=WebSocketManager.currentPart.currency
                marketWs.issuer=WebSocketManager.currentPart.issuer
                marketWs.baseCurrency=WebSocketManager.currentPart.baseCurrency
                marketWs.baseIssuer=WebSocketManager.currentPart.baseIssuer
                it.sendText(Gson().toJson(marketWs))
            }
        }
    }

    override fun onFrameSent(websocket: WebSocket?, frame: WebSocketFrame?) {
    }

    override fun onThreadStopping(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
    }

    override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
    }

    override fun onMessageDecompressionError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        compressed: ByteArray?
    ) {
    }

    override fun onPongFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
    }

    override fun onMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frames: MutableList<WebSocketFrame>?
    ) {
    }
}