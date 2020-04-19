//package com.eks.pagingrecyclerview
//
//import android.content.Context
//import android.util.AttributeSet
//import android.util.Log
//import android.view.MotionEvent
//import android.view.ViewConfiguration
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import java.math.BigDecimal
//import java.math.BigDecimal.ROUND_CEILING
//
///**
// *
// * Created by ZhongXi_Lv on 2020/4/17.
// */
//class PagingRecyclerView @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : RecyclerView(context, attrs, defStyleAttr) {
//    /**
//     * 每一页的容量
//     */
//    var pageSize = 0
//
//    /**
//     * 一共多少页
//     */
//    var pageCount = 0
//
//    /**
//     * 当前页码
//     */
//    var currentPage = 0
//
//    /**
//     * 滑动距离起步值
//     */
//    private var scaledTouchSlop = 0
//
//    /**
//     * 按下的X值
//     */
//    var downX = 0f
//
//    /**
//     * 松手的X值
//     */
//    var upX = 0f
//
//    /**
//     * X差值
//     */
//    var diffX = 0f
//
//    private lateinit var gridLayoutManager: GridLayoutManager
//
//    init {
//        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
//    }
//
//    override fun onTouchEvent(e: MotionEvent): Boolean {
//
//        when (e.action) {
//            MotionEvent.ACTION_DOWN -> {
//                downX = e.x
//                diffX = 0f
//                downCurrentPage()
//            }
//            MotionEvent.ACTION_MOVE -> {
//            }
//            MotionEvent.ACTION_UP,
//            MotionEvent.ACTION_CANCEL -> {
//                upX = e.x
//                diffX = upX - downX
//                if (diffX > 0) {// 左滑
//                    // 如果当前处于第一页, 没必要往下走了
//                    if (currentPage == 0) {
//                        troggleTurning(DRAGGING_LEFT_RESET)
//                        return super.onTouchEvent(e)
//                    }
//                    if (diffX > scaledTouchSlop) {// 拉够了判定上一页
//                        troggleTurning(DRAGGING_LEFT_BACK)
//                    } else {// 拉不够判定复位
//                        troggleTurning(DRAGGING_RIGHT_RESET)
//                    }
//                } else if (diffX < 0) {// 右滑
//                    // 如果当前处于最后一页, 没必要往下走了
//                    if (currentPage == pageCount - 1) {
//                        troggleTurning(DRAGGING_RIGHT_RESET)
//                        return super.onTouchEvent(e)
//                    }
//                    if (-diffX > scaledTouchSlop) {// 拉够了判定下一页
//                        troggleTurning(DRAGGING_RIGHT_NEXT)
//                    } else {// 拉不够判定复位
//                        troggleTurning(DRAGGING_LEFT_RESET)
//                    }
//                    fling(0, 0)
//                } else {// 没动
//                    return super.onTouchEvent(e)
//                }
//            }
//        }
//        return super.onTouchEvent(e)
//    }
//
//    /**
//     * 按下手指时页码
//     */
//    private fun downCurrentPage() {
//        gridLayoutManager = layoutManager as GridLayoutManager
//        // 获取第一个完全可视的位置
//        val findFirstCompletelyVisibleItemPosition =
//            gridLayoutManager.findFirstCompletelyVisibleItemPosition()
//        val findLastCompletelyVisibleItemPosition =
//            gridLayoutManager.findLastCompletelyVisibleItemPosition()
//        currentPage = if ((findLastCompletelyVisibleItemPosition + 1) == adapter?.itemCount ?: 0) {
//            // 如果当前已经在最末尾, 页码就是最后一页
//            pageCount - 1
//        } else {
//            // 否则就根据位置判断当前处于第几页
//            (findFirstCompletelyVisibleItemPosition) / pageSize
//        }
//    }
//
//    /**
//     * 翻页触发
//     */
//    private fun troggleTurning(flag: Int) {
//        var position = 0
//        when (flag) {
//            DRAGGING_LEFT_BACK -> {
//                position = getPositionByPage(currentPage - 1)
//                smoothScrollToPosition(position)
//                Log.i("2333", "左翻页")
//            }
//            DRAGGING_RIGHT_NEXT -> {
//                position = getPositionByPage(currentPage + 1) + 4
//                smoothScrollToPosition(position)
//                Log.i("2333", "右翻页")
//            }
//            DRAGGING_LEFT_RESET, DRAGGING_RIGHT_RESET -> {
//                position = getPositionByPage(currentPage)
//                onScrollStateChanged(SCROLL_STATE_IDLE)
//                Log.i("2333", "复位")
//                needResetPosition = position
//            }
//        }
//    }
//
//    /**
//     * 上次状态
//     */
//    var lastState = 0
//
//    /**
//     * 需要复位的位置
//     */
//    var needResetPosition = -1
//
//    override fun onScrollStateChanged(state: Int) {
//        super.onScrollStateChanged(state)
//        when (state) {
//            SCROLL_STATE_IDLE -> {
//                if (lastState == SCROLL_STATE_SETTLING) {
//                    if (needResetPosition != -1) {
//                        gridLayoutManager.scrollToPositionWithOffset(needResetPosition, 0)
//                        needResetPosition = -1
//                    }
//                }
//            }
//        }
//        lastState = state
//    }
//
//    private fun getPositionByPage(page: Int): Int = page * pageSize
//
//    /**
//     * 初始化分页信息
//     */
//    private fun initPageCounts() {
//        if (pageCount == 0) {
//            val itemCount = BigDecimal(adapter?.itemCount ?: 0)
//            val pageItemCount = BigDecimal(pageSize)
//            pageCount = itemCount.divide(pageItemCount, ROUND_CEILING).toInt()
//        }
//    }
//
//
//    override fun setAdapter(adapter: Adapter<*>?) {
//        super.setAdapter(adapter)
//        adapter?.registerAdapterDataObserver(adapterObserver)
//    }
//
//    /**
//     * 创建一个观察者
//     * 因为每次notifyDataChanged的时候，系统都会调用这个观察者的onChange函数
//     */
//    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
//        override fun onChanged() {
//            super.onChanged()
//            initPageCounts()
//        }
//    }
//
//    companion object {
//        /**
//         * 往左滑上一位
//         */
//        const val DRAGGING_LEFT_BACK = 0
//
//        /**
//         * 往右滑下一位
//         */
//        const val DRAGGING_RIGHT_NEXT = 1
//
//        /**
//         * 复位
//         */
//        const val DRAGGING_LEFT_RESET = -1
//
//        /**
//         * 复位
//         */
//        const val DRAGGING_RIGHT_RESET = -2
//    }
//}