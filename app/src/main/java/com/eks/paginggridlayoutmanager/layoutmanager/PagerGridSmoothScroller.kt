package com.eks.paginggridlayoutmanager.layoutmanager

import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * 作用：用于处理平滑滚动
 * 摘要：用于用户手指抬起后页面对齐或者 Fling 事件。
 *
 * 原作者:GcsSloop
 */
class PagerGridSmoothScroller(private val recyclerView: RecyclerView) :
    LinearSmoothScroller(recyclerView.context) {
    override fun onTargetFound(
        targetView: View,
        state: RecyclerView.State,
        action: Action
    ) {
        val manager = recyclerView.layoutManager ?: return
        if (manager is PagerGridLayoutManager) {
            val pos = recyclerView.getChildAdapterPosition(targetView)
            val snapDistances = manager.getSnapOffset(pos)
            val dx = snapDistances[0]
            val dy = snapDistances[1]
            val time = calculateTimeForScrolling(
                abs(dx).coerceAtLeast(abs(dy))
            )
            if (time > 0) {
                action.update(dx, dy, time, mDecelerateInterpolator)
            }
        }
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return PagerConfig.getMillisecondsPreInch() / displayMetrics.densityDpi
    }

}