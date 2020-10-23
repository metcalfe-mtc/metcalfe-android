package com.lang.ktn.utils

import android.view.View
import android.widget.PopupWindow
import kotlinx.android.extensions.LayoutContainer
import java.io.Serializable

class MyPop: PopupWindow,LayoutContainer{

   override lateinit var  containerView: View

   constructor(contentView: View,width:Int,height:Int):super(contentView,width,height){
      containerView=contentView
   }
//   View contentView, int width, int height)
}
