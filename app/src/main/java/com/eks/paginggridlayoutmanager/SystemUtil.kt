package com.eks.paginggridlayoutmanager

import android.content.Context

/**
 *
 * Created by ZhongXi_Lv on 2020/4/17.
 */
object SystemUtil {
    var screenWidth = 0
    var screenHeight = 0
    var screenDensity = 0f

    fun init(context: Context) {
        val metric = context.resources.displayMetrics
        screenWidth = metric.widthPixels
        screenHeight = metric.heightPixels
        screenDensity = metric.density
    }
}