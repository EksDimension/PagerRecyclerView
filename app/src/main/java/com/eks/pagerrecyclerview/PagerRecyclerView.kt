package com.eks.pagerrecyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import java.math.BigDecimal.ROUND_CEILING
import kotlin.math.abs

/**
 * 支持横向分页RecyclerView
 * Created by Riggs on 2020/4/17.
 */
open class PagerRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    /**
     * 每一页的容量
     */
    @JvmField
    var pageSize: Int = 0

    /**
     * 一共多少页
     */
    private var pageCount = 0

    /**
     * 当前页码
     */
    private var currentPage = 0

    /**
     * 滑动距离起步值
     */
    private var scaledTouchSlop = 0

    /**
     * 按下的X值
     */
//    private var downX = 0f

    /**
     * 松手的X值
     */
    private var upX = 0f

    /**
     * X差值
     */
    private var diffX = 0f

    /**
     * 上次状态
     */
    private var lastState = 0

    /**
     * 需要复位的位置
     */
    private var needResetPosition = -1

    /**
     * 本次拖拽滚动距离
     */
    private var dragX = 0f

    private lateinit var gridLayoutManager: GridLayoutManager

    private var pagingHandler = Handler()

    private var onPositionChangedListener: OnPositionChangedListener? = null

    init {
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
//        scaledTouchSlop = 0
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        dragX += dx
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
//                downX = e.x
                dragX = 0f
                diffX = 0f
                downCurrentPage()
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                // 如果是已复位, 则用移动后的第一下记录位置.
                // 本来这种逻辑应该在ACTION_DOWN里面做, 但是在我们的viewpager里面 ACTION_DOWN无法处理, 只好在这了.
                /*if (downX == 0f) {
                    downX = e.x
                    diffX = 0f
                    downCurrentPage()
                }*/
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                stopScroll()
//                e.action = MotionEvent.ACTION_CANCEL
//                upX = e.x
//                diffX = upX - downX
//                diffX = e.x - dragX
                if (dragX < 0) {// 左滑
                    // 如果没拖动,没必要走
                    if (dragX == 0f) {
//                        troggleTurning(DRAGGING_LEFT_RESET)
//                        reset()
                        return super.onTouchEvent(e)
                    }
                    if (abs(dragX) > scaledTouchSlop) {// 拉够了判定上一页
                        troggleTurning(DRAGGING_LEFT_BACK)
                    } else {// 拉不够判定复位
                        troggleTurning(DRAGGING_RIGHT_RESET)
                    }
                } else if (dragX > 0) {// 右滑
                    // 如果没拖动,没必要走
                    if (dragX == 0f) {
//                        troggleTurning(DRAGGING_RIGHT_RESET)
//                        reset()
                        return super.onTouchEvent(e)
                    }
                    if (abs(dragX) > scaledTouchSlop) {// 拉够了判定下一页
                        troggleTurning(DRAGGING_RIGHT_NEXT)
                    } else {// 拉不够判定复位
                        troggleTurning(DRAGGING_LEFT_RESET)
                    }
//                    fling(0, 0)
                } else {// 没动
//                    reset()
                    return super.onTouchEvent(e)
                }
            }
        }
        return super.onTouchEvent(e)
    }

    /**
     * 恢复X值
     */
    /*fun reset() {
        downX = 0f
        diffX = 0f
        downCurrentPage()
    }*/

    /**
     * 按下手指时页码
     */
    private fun downCurrentPage() {
        // 获取第一个完全可视的位置
        val findFirstCompletelyVisibleItemPosition =
            gridLayoutManager.findFirstCompletelyVisibleItemPosition()
        val findLastCompletelyVisibleItemPosition =
            gridLayoutManager.findLastCompletelyVisibleItemPosition()
        currentPage = if ((findLastCompletelyVisibleItemPosition + 1) == adapter?.itemCount ?: 0) {
            // 如果当前已经在最末尾, 页码就是最后一页
            pageCount - 1
        } else {
            // 否则就根据位置判断当前处于第几页
            (findFirstCompletelyVisibleItemPosition) / pageSize
        }
    }

    /**
     * 翻页触发
     */
    private fun troggleTurning(flag: Int) {
        pagingHandler.removeCallbacksAndMessages(null)
        pagingHandler.postDelayed({
            var position = 0
            when (flag) {
                DRAGGING_LEFT_BACK -> {
                    position = getPositionByPage(currentPage - 1)
                    smoothScrollToPosition(position)
//                    Log.i(TAG, "左翻页$position")
                }
                DRAGGING_RIGHT_NEXT -> {
                    position = getPositionByPage(currentPage + 1) + (pageSize-1)
                    smoothScrollToPosition(position)
//                    Log.i(TAG, "右翻页$position")
                }
                DRAGGING_LEFT_RESET, DRAGGING_RIGHT_RESET -> {
                    position = getPositionByPage(currentPage)
                    onScrollStateChanged(SCROLL_STATE_IDLE)
//                    Log.i(TAG, "复位$position")
                    needResetPosition = position
                    gridLayoutManager.scrollToPositionWithOffset(needResetPosition, 0)
                }
            }
        }, DELAY_TIME)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        gridLayoutManager = layout as GridLayoutManager
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state) {
            SCROLL_STATE_IDLE -> {
                if ((lastState == SCROLL_STATE_SETTLING || lastState == SCROLL_STATE_DRAGGING) && lastState == SCROLL_STATE_IDLE) {
                    onPositionChangedListener?.onPositionChanged(currentPage)
                }
            }
        }
        lastState = state
    }

    private fun getPositionByPage(page: Int): Int = page * pageSize

    /**
     * 初始化分页信息
     */
    private fun initPageCounts() {
        if(pageSize==0){
            throw IllegalArgumentException("pageSize is not set yet. ensure that you have set pageSize before executing setAdatper(adapter)")
        }
        if (pageCount == 0) {
            val itemCount = BigDecimal(adapter?.itemCount ?: 0)
            val pageItemCount = BigDecimal(pageSize)
            pageCount = itemCount.divide(pageItemCount, ROUND_CEILING).toInt()
        }
    }


    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(adapterObserver)
    }

    /**
     * 创建一个观察者
     * 因为每次notifyDataChanged的时候，系统都会调用这个观察者的onChange函数
     */
    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            initPageCounts()
        }
    }

    fun setOnPositionChangedListener(onPositionChangedListener: OnPositionChangedListener) {
        this.onPositionChangedListener = onPositionChangedListener
    }

    /**
     * 位置变化接口
     */
    interface OnPositionChangedListener {
        fun onPositionChanged(selectedPosition: Int)
    }

    companion object {
        /**
         * 往左滑上一位
         */
        const val DRAGGING_LEFT_BACK = 0

        /**
         * 往右滑下一位
         */
        const val DRAGGING_RIGHT_NEXT = 1

        /**
         * 复位
         */
        const val DRAGGING_LEFT_RESET = -1

        /**
         * 复位
         */
        const val DRAGGING_RIGHT_RESET = -2

        /**
         * 延迟触发时间
         */
        const val DELAY_TIME = 100L

        val TAG = PagerRecyclerView::class.simpleName
    }
}