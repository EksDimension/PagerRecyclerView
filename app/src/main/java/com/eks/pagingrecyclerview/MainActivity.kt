package com.eks.pagingrecyclerview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eks.pagingrecyclerview.paginglayoutmanager.PagerGridLayoutManager
import com.eks.pagingrecyclerview.paginglayoutmanager.PagerGridLayoutManager.HORIZONTAL
import com.eks.pagingrecyclerview.paginglayoutmanager.PagerGridSnapHelper
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
        pagerGridLayoutManager?.registerOnPageChangeCallback { position, positionOffset, positionOffsetPixels ->
            Log.i(
                "233",
                "当前页码:$position 偏移量:$positionOffset 偏移百分比$positionOffsetPixels"
            )
        }
    }
}
