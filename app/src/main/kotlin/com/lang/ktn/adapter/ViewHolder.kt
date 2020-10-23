package com.lang.ktn.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

//    fun bind(title: String) {
//        itemTitle.text = "Hello Kotlin!"
//    }
}