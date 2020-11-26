package com.me.rulerview

import android.graphics.Color

/**
 * @author dysen
 * dy.sen@qq.com     11/26/20 3:09 PM
 *
 *
 * Info：
 */
object RulerColor {
    /**
     * 计算渐变颜色中间色值
     *
     * @param startColor 起始颜色
     * @param endColor   结束颜色
     * @param radio      百分比，取值范围【0~1】
     * @return 颜色值
     */
    fun getColor(startColor: Int, endColor: Int, radio: Float): Int {
        val redStart = Color.red(startColor)
        val blueStart = Color.blue(startColor)
        val greenStart = Color.green(startColor)
        val redEnd = Color.red(endColor)
        val blueEnd = Color.blue(endColor)
        val greenEnd = Color.green(endColor)
        val red = (redStart + ((redEnd - redStart) * radio + 0.5)).toInt()
        val greed = (greenStart + ((greenEnd - greenStart) * radio + 0.5)).toInt()
        val blue = (blueStart + ((blueEnd - blueStart) * radio + 0.5)).toInt()
        return Color.argb(255, red, greed, blue)
    }
}