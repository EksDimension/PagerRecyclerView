package com.eks.pagerrecyclerview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

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
        rv.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        rv.pageSize = 10
        rv.adapter = mainAdapter
    }

    private fun initData() {
        val dataList = ArrayList<String>()
        for (i in 1..25) {
            dataList.add("$i")
        }
        mainAdapter?.dataList = dataList
        mainAdapter?.notifyDataSetChanged()
    }

    private fun initListener() {
//        pagerGridLayoutManager?.registerOnPageChangeCallback (object :PagerGridLayoutManager.OnPageChangeCallback{
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                Log.i(
//                    "233",
//                    "当前页码:$position 偏移百分比:$positionOffset 偏移量:$positionOffsetPixels"
//                )
//            }
//        })
        rv.setOnPositionChangedListener(object : PagerRecyclerView.OnPositionChangedListener {
            override fun onPositionChanged(selectedPosition: Int) {
                Log.i(TAG, "当前页码:$selectedPosition")
            }
        })
    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}
