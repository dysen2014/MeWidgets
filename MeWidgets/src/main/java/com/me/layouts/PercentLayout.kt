package com.me.layouts

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.me.widgets.R

/**
 * dysen.
 * dy.sen@qq.com    11/3/20  10:06 AM
 *
 *
 * Info： 百分比布局
 */
class PercentLayout : RelativeLayout {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取父容器的尺寸
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            val params = child.layoutParams
            //如果说是百分比布局属性
            if (checkLayoutParams(params)) {
                val lp = params as LayoutParams
                val widthPercent = lp.widthPercent
                val heightPercent = lp.heightPercent
                val marginLeftPercent = lp.marginLeftPercent
                val marginRightPercent = lp.marginRightPercent
                val marginTopPercent = lp.marginTopPercent
                val marginBottomPercent = lp.marginBottomPercent
                if (widthPercent > 0) {
                    params.width = (widthSize * widthPercent).toInt()
                }
                if (widthPercent > 0) {
                    params.height = (heightSize * heightPercent).toInt()
                }
                if (widthPercent > 0) {
                    params.leftMargin = (widthSize * marginLeftPercent).toInt()
                }
                if (widthPercent > 0) {
                    params.rightMargin = (widthSize * marginRightPercent).toInt()
                }
                if (widthPercent > 0) {
                    params.topMargin = (heightSize * marginTopPercent).toInt()
                }
                if (widthPercent > 0) {
                    params.bottomMargin = (heightSize * marginBottomPercent).toInt()
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    class LayoutParams(c: Context, attrs: AttributeSet?) : RelativeLayout.LayoutParams(c, attrs) {
        internal val widthPercent: Float
        internal val heightPercent: Float
        internal val marginLeftPercent: Float
        internal val marginRightPercent: Float
        internal val marginTopPercent: Float
        internal val marginBottomPercent: Float

        init {
            //解析自定义属性
            val a = c.obtainStyledAttributes(attrs, R.styleable.PercentLayout)
            widthPercent = a.getFloat(R.styleable.PercentLayout_widthPercent, 0f)
            heightPercent = a.getFloat(R.styleable.PercentLayout_heightPercent, 0f)
            marginLeftPercent = a.getFloat(R.styleable.PercentLayout_marginLeftPercent, 0f)
            marginRightPercent = a.getFloat(R.styleable.PercentLayout_marginRightPercent, 0f)
            marginTopPercent = a.getFloat(R.styleable.PercentLayout_marginTopPercent, 0f)
            marginBottomPercent = a.getFloat(R.styleable.PercentLayout_marginBottomPercent, 0f)
            a.recycle()
        }
    }
}