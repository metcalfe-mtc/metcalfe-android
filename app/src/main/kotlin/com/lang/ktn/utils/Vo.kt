package com.lang.ktn.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlinx.android.extensions.LayoutContainer

class Vo @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    override val containerView: View?
) : View(context, attrs, defStyleAttr), LayoutContainer {
}