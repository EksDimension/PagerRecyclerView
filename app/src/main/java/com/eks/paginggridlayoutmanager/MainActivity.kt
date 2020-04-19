package com.eks.paginggridlayoutmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eks.paginggridlayoutmanager.layoutmanager.PagerGridLayoutManager
import com.eks.paginggridlayoutmanager.layoutmanager.PagerGridLayoutManager.Companion.HORIZONTAL
import com.eks.paginggridlayoutmanager.layoutmanager.PagerGridSnapHelper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var pagerGridLayoutManager: PagerGridLayoutManager? = null
    var mainAdapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initAdapter()
        initListener()
        initData()
    }

    private fun initView() {
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()
        pagerGridLayoutManager = PagerGridLayoutManager(1, 5, HORIZONTAL)
        rv.layoutManager = pagerGridLayoutManager
        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(rv)
        rv.adapter = mainAdapter
    }

    private fun initData() {
        val dataList = ArrayList<String>()
        for (i in 1..10) {
            dataList.add("$i")
        }
        mainAdapter?.dataList = dataList
        mainAdapter?.notifyDataSetChanged()
    }

    private fun initListener() {
        pagerGridLayoutManager?.registerOnPageChangeCallback (object :PagerGridLayoutManager.OnPageChangeCallback{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.i(
                    "233",
                    "当前页码:$position 偏移百分比:$positionOffset 偏移量:$positionOffsetPixels"
                )
            }
        })
    }
}
