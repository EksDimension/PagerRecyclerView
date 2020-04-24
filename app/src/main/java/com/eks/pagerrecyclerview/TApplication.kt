package com.eks.pagerrecyclerview

import android.app.Application

/**
 *
 * Created by ZhongXi_Lv on 2020/4/17.
 */
class TApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SystemUtil.init(this)
    }
}