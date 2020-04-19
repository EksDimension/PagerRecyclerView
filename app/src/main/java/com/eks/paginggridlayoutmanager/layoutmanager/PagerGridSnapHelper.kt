package com.eks.paginggridlayoutmanager.layoutmanager

import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
import androidx.recyclerview.widget.SnapHelper
import kotlin.math.abs

/**
 * 作用：分页居中工具
 * 摘要：每次只滚动一个页面
 *
 * 原作者:GcsSloop
 */
class PagerGridSnapHelper : SnapHelper() {
    private var recyclerView // RecyclerView
            : RecyclerView? = null

    /**
     * 用于将滚动工具和 Recycler 绑定
     *
     * @param recyclerView RecyclerView
     * @throws IllegalStateException 状态异常
     */
    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    /**
     * 计算需要滚动的向量，用于页面自动回滚对齐
     *
     * @param layoutManager 布局管理器
     * @param targetView    目标控件
     * @return 需要滚动的距离
     */
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        val pos = layoutManager.getPosition(targetView)
        var offset = IntArray(2)
        if (layoutManager is PagerGridLayoutManager) {
            offset = layoutManager.getSnapOffset(pos)
        }
        return offset
    }

    /**
     * 获得需要对齐的View，对于分页布局来说，就是页面第一个
     *
     * @param layoutManager 布局管理器
     * @return 目标控件
     */
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager is PagerGridLayoutManager) {
            return layoutManager.findSnapView()
        }
        return null
    }

    /**
     * 获取目标控件的位置下标
     * (获取滚动后第一个View的下标)
     *
     * @param layoutManager 布局管理器
     * @param velocityX     X 轴滚动速率
     * @param velocityY     Y 轴滚动速率
     * @return 目标控件的下标
     */
    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int, velocityY: Int
    ): Int {
        var target = RecyclerView.NO_POSITION
        if (layoutManager is PagerGridLayoutManager) {
            if (layoutManager.canScrollHorizontally()) {
                if (velocityX > PagerConfig.getFlingThreshold()) {
                    target = layoutManager.findNextPageFirstPos()
                } else if (velocityX < -PagerConfig.getFlingThreshold()) {
                    target = layoutManager.findPrePageFirstPos()
                }
            } else if (layoutManager.canScrollVertically()) {
                if (velocityY > PagerConfig.getFlingThreshold()) {
                    target = layoutManager.findNextPageFirstPos()
                } else if (velocityY < -PagerConfig.getFlingThreshold()) {
                    target = layoutManager.findPrePageFirstPos()
                }
            }
        }
        return target
    }

    /**
     * 一扔(快速滚动)
     *
     * @param velocityX X 轴滚动速率
     * @param velocityY Y 轴滚动速率
     * @return 是否消费该事件
     */
    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        val layoutManager = recyclerView?.layoutManager ?: return false
        recyclerView?.adapter ?: return false
        val minFlingVelocity = PagerConfig.getFlingThreshold()
        return ((abs(velocityY) > minFlingVelocity || abs(velocityX) > minFlingVelocity)
                && snapFromFling(layoutManager, velocityX, velocityY))
    }

    /**
     * 快速滚动的具体处理方案
     *
     * @param layoutManager 布局管理器
     * @param velocityX     X 轴滚动速率
     * @param velocityY     Y 轴滚动速率
     * @return 是否消费该事件
     */
    private fun snapFromFling(
        layoutManager: RecyclerView.LayoutManager, velocityX: Int,
        velocityY: Int
    ): Boolean {
        if (layoutManager !is ScrollVectorProvider) {
            return false
        }
        val smoothScroller = createSnapScroller(layoutManager) ?: return false
        val targetPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if (targetPosition == RecyclerView.NO_POSITION) {
            return false
        }
        smoothScroller.targetPosition = targetPosition
        layoutManager.startSmoothScroll(smoothScroller)
        return true
    }

    /**
     * 通过自定义 LinearSmoothScroller 来控制速度
     *
     * @param layoutManager 布局故哪里去
     * @return 自定义 LinearSmoothScroller
     */
    override fun createSnapScroller(layoutManager: RecyclerView.LayoutManager): LinearSmoothScroller? {
        return if (layoutManager !is ScrollVectorProvider) {
            null
        } else {
            recyclerView?.let { PagerGridSmoothScroller(it) }
        }
    }
    //--- 公开方法 ----------------------------------------------------------------------------------
    /**
     * 设置滚动阀值
     * @param threshold 滚动阀值
     */
    fun setFlingThreshold(threshold: Int) {
        PagerConfig.setFlingThreshold(threshold)
    }
}