package com.eks.pagerrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_adapter_item_layout.view.*

/**
 * Created by ZhongXi_Lv on 2020/4/17.
 */
class MainAdapter : RecyclerView.Adapter<MainAdapter.VH>() {

    var dataList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_adapter_item_layout, parent, false)
        return VH(itemView)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.tvText.text = dataList[position]
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            val lp = itemView.layoutParams as RecyclerView.LayoutParams
            val eachItemWidth = SystemUtil.screenWidth / 5
            lp.width = eachItemWidth
            itemView.layoutParams = lp
        }
    }
}