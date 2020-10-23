package com.lang.ktn.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lang.ktn.bean.Model
import com.lang.progrom.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

class Kt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val model: Model = intent.getParcelableExtra("data")

        txt.text = model.title

    }

}


fun cost(block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    println(System.currentTimeMillis() - start)
}


fun main(args: Array<String>) {

    cost {

    }

}