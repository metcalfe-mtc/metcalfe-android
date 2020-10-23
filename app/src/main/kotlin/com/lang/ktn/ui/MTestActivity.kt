package com.lang.ktn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.lang.ktn.bean.Model
//import com.lang.progrom.BuildConfig
import com.lang.progrom.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.android.HandlerDispatcher
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter
/*
 * https://kotlinlang.org/docs/tutorials/android-plugin.html
**/
class MTestActivity : AppCompatActivity() {

//    //封装的界面刷新方法，不用每次都调用launch(UI)了
//    fun launchUi(start: CoroutineStart = CoroutineStart.DEFAULT,
//                 parent: Job? = null,
//                 block: suspend CoroutineScope.() -> Unit) = launch(UI, start, parent, block)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txt.text="liu da ge "
        txt.text="button liu da ge "
        button.setOnClickListener {
            if(it is Button){
                it.text="hahah"


                GlobalScope.launch{

                }


//                val intent = Intent(this, Kt::class.java)
//                intent.putExtra("data", Model(BuildConfig.BASE_URL,886))
//                startActivity(intent)


//                val UI = HandlerDispatcher
//                //启动线程
//                val job = launch {
//                    //加载数据任务一
//                    val load = loadData()
//                    //刷新界面
//                    launch(Main) {
//                        Toast.makeText(this@MTestActivity, load, Toast.LENGTH_SHORT).show()
//                        Log.d("launch", load)
//                    }
//                    //加载数据任务二
//                    val select = selectData()
//                    launch(Main) {
//                        Toast.makeText(this@MTestActivity, select, Toast.LENGTH_SHORT).show()
//                        Log.d("launch", select)
//                    }
//                }
//
//
//
//                val coroutineDispatcher = newSingleThreadContext("ctx")
//                // 启动协程 1
//                GlobalScope.launch(HandlerContext()) {
//                    Log.e("INFO","the first coroutine")
//                    delay(3000)
//                    Log.e("INFO","the first coroutine")
//                    it.text="he first coroutine"
//                }
//
//                // 启动协程 2
//                GlobalScope.launch(coroutineDispatcher) {
//                    Log.e("INFO","the second coroutine")
//                    delay(5000)
//                    Log.e("INFO","the second coroutine")
//                    it.text="he second coroutine"
//                }
//
//

//                com.lang.ktn.net.Requester.apiService().getTabDiscovery().execute().code()==200
//
//                suspend fun main() = coroutineScope() {
//                    launch {
//                        delay(1000)
//                        println("Kotlin Coroutines World!")
//                    }
//                    println("Hello")
//                }

//                CoroutineScope.launch


//                fun <T> CoroutineScope.async(
//                    context: CoroutineContext = EmptyCoroutineContext,
//                    start: CoroutineStart = CoroutineStart.DEFAULT,
//                    block: suspend CoroutineScope.() -> T
//                ): Deferred<T> (source)
//
//interface Deferred<out T> : Job(source)





            }
        }

    }
}
