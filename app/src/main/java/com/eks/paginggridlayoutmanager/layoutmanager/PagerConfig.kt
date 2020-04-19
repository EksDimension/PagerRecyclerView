package com.eks.paginggridlayoutmanager.layoutmanager

/**
 * 作用：Pager配置
 * 摘要：主要用于Log的显示与关闭
 *
 * 原作者GcsSloop
 */
class PagerConfig {
    companion object {
        private const val TAG = "PagerGrid"
        private var sShowLog = false
        private var sFlingThreshold = 1000 // Fling 阀值，滚动速度超过该阀值才会触发滚动
        private var sMillisecondsPreInch = 60f // 每一个英寸滚动需要的微秒数，数值越大，速度越慢
        /**
         * 判断是否输出日志
         *
         * @return true 输出，false 不输出
         */
        fun isShowLog(): Boolean {
            return PagerConfig.sShowLog
        }

        /**
         * 设置是否输出日志
         *
         * @param showLog 是否输出
         */
        fun setShowLog(showLog: Boolean) {
            PagerConfig.sShowLog = showLog
        }

        /**
         * 获取当前滚动速度阀值
         *
         * @return 当前滚动速度阀值
         */
        fun getFlingThreshold(): Int {
            return PagerConfig.sFlingThreshold
        }

        /**
         * 设置当前滚动速度阀值
         *
         * @param flingThreshold 滚动速度阀值
         */
        fun setFlingThreshold(flingThreshold: Int) {
            PagerConfig.sFlingThreshold = flingThreshold
        }

        /**
         * 获取滚动速度 英寸/微秒
         *
         * @return 英寸滚动速度
         */
        fun getMillisecondsPreInch(): Float {
            return PagerConfig.sMillisecondsPreInch
        }

        /**
         * 设置像素滚动速度 英寸/微秒
         *
         * @param millisecondsPreInch 英寸滚动速度
         */
        fun setMillisecondsPreInch(millisecondsPreInch: Float) {
            PagerConfig.sMillisecondsPreInch = millisecondsPreInch
        }

    }
}