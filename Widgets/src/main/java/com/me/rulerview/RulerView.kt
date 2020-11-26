package com.me.rulerview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.annotation.ColorInt
import com.me.rulerview.RulerView

/**
 * @author dysen
 * dy.sen@qq.com     11/26/20 3:07 PM
 *
 * Info：
 */
class RulerView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        val TAG = RulerView::class.java.simpleName

        /**
         * 控件宽高
         */
        var width = 0
        var height = 0

        /**
         * 渐变色起始色
         */
        @ColorInt
        var startColor = Color.parseColor("#FFEBEE")

        /**
         * 渐变色结束色
         */
        @ColorInt
        var endColor = Color.parseColor("#D50000")

        /**
         * 指示器颜色
         */
        @ColorInt
        var indicatorColor = startColor

        /**
         * 控件绘制画笔
         */
        var paint: Paint? = null

        /**
         * 文字画笔
         */
        var textPaint: Paint? = null

        /**
         * 线条宽度，默认12px
         */
        var lineWidth = 24

        /**
         * 长、中、短刻度的高度
         */
        var maxLineHeight = 0
        var midLineHeight = 0
        var minLineHeight = 0

        /**
         * 指示器半径
         */
        var indicatorRadius = lineWidth / 2

        /**
         * 刻度尺的开始、结束数字
         */
        var startNum = 0
        var endNum = 100

        /**
         * 每个刻度代表的数字单位
         */
        var unitNum = 1

        /**
         * 刻度间隔
         */
        var lineSpacing = 3 * lineWidth

        /**
         * 第一刻度位置距离当前位置的偏移量，一定小于0
         */
        var offsetStart = 0f

        /**
         * 辅助计算滑动，主要用于惯性计算
         */
        var scroller: Scroller? = null

        /**
         * 跟踪用户手指滑动速度
         */
        var velocityTracker: VelocityTracker? = null

        /**
         * 定义惯性作用的最小速度
         */
        var minVelocityX = 0f

        /**
         * 刻度文字大小
         */
        var textSize = 96

        /**
         * 文字高度
         */
        var textHeight = 0f

        /**
         * 当前选中数字
         */
        var curSelectNum = 0
    }

    var listener: OnNumSelectListener? = null

    interface OnNumSelectListener {
        fun onNumSelect(selectedNum: Int)
    }

    fun setOnNumSelectListener(listener: OnNumSelectListener?) {
        this.listener = listener
    }

    fun initView(context: Context?, attrs: AttributeSet?) {
        paint = Paint()
        paint!!.color = startColor
        paint!!.style = Paint.Style.FILL
        textPaint = Paint()
        textPaint!!.color = startColor
        textPaint!!.style = Paint.Style.FILL
        textPaint!!.textSize = textSize.toFloat()
        textPaint!!.typeface = Typeface.DEFAULT_BOLD
        val fontMetrics = textPaint!!.fontMetrics
        textHeight = fontMetrics.descent - fontMetrics.ascent
        scroller = Scroller(context)
        velocityTracker = VelocityTracker.obtain()
        minVelocityX = ViewConfiguration.get(getContext()).scaledMinimumFlingVelocity.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //绘制刻度
        for (i in 0 until (endNum - startNum) / unitNum + 1) {
            var lineHeight = minLineHeight
            if (i % 10 == 0) {
                lineHeight = maxLineHeight
            } else if (i % 5 == 0) {
                lineHeight = midLineHeight
            }
            val lineLeft =
                offsetStart + movedX + Companion.width / 2 - lineWidth / 2 + i * lineSpacing
            val lineRight = lineLeft + lineWidth
            val rectF =
                RectF(lineLeft, (4 * indicatorRadius).toFloat(), lineRight, lineHeight.toFloat())
            paint!!.color = RulerColor.getColor(
                startColor,
                endColor,
                i.toFloat() / ((endNum - startNum) / unitNum).toFloat()
            )
            canvas.drawRoundRect(
                rectF,
                (lineWidth / 2).toFloat(),
                (lineWidth / 2).toFloat(),
                paint!!
            )

            //绘制刻度文字
            if (i % 10 == 0) {
                textPaint!!.color = RulerColor.getColor(
                    startColor,
                    endColor,
                    i.toFloat() / ((endNum - startNum) / unitNum).toFloat()
                )
                canvas.drawText(
                    i.toString() + "",
                    lineLeft + lineWidth / 2 - textPaint!!.measureText("" + i) / 2,
                    lineHeight + 20 + textHeight,
                    textPaint!!
                )
            }
        }

        //draw indicator
        val indicatorX = Companion.width / 2
        val indicatorY = indicatorRadius
        indicatorColor = RulerColor.getColor(
            startColor,
            endColor,
            Math.abs((offsetStart + movedX) / (lineSpacing * ((endNum - startNum) / unitNum)).toFloat())
        )
        paint!!.color = indicatorColor
        canvas.drawCircle(
            indicatorX.toFloat(),
            indicatorY.toFloat(),
            indicatorRadius.toFloat(),
            paint!!
        )

//        postInvalidate();
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //layout之后获取控件实际宽高
        Companion.width = w
        Companion.height = h
        //最长刻度线的长度默认为控件总高度的2/3
        maxLineHeight = Companion.height * 2 / 3
        midLineHeight = maxLineHeight * 4 / 5
        minLineHeight = maxLineHeight * 3 / 5
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    val selectedNum: Int
        get() = (Math.abs(offsetStart + movedX) / lineSpacing).toInt()

    /**
     * 用户手指按下控件滑动时的初始位置坐标
     */
    var downX = 0f

    /**
     * 当前手指移动的距离
     */
    var movedX = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        velocityTracker!!.addMovement(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scroller!!.forceFinished(true)
                downX = event.x
                movedX = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                movedX = event.x - downX
                Log.i(TAG, "offsetStart==>$offsetStart")
                Log.i(TAG, "movedX==>$movedX")
                Log.i(TAG, "offsetStart + movedX==>" + (offsetStart + movedX))
                //边界控制
                if (offsetStart + movedX > 0) {
                    movedX = 0f
                    offsetStart = 0f
                } else if (offsetStart + movedX < -((endNum - startNum) / unitNum) * lineSpacing) {
                    offsetStart = (-((endNum - startNum) / unitNum) * lineSpacing).toFloat()
                    movedX = 0f
                }
                if (listener != null) {
                    Log.i(TAG, "getSelectedNum()==>$selectedNum")
                    listener!!.onNumSelect(selectedNum)
                }
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (offsetStart + movedX <= 0 && offsetStart + movedX >= -((endNum - startNum) / unitNum) * lineSpacing) {
                    //手指松开时需要磁吸效果
                    offsetStart = offsetStart + movedX
                    movedX = 0f
                    offsetStart = ((offsetStart / lineSpacing).toInt() * lineSpacing).toFloat()
                } else if (offsetStart + movedX > 0) {
                    movedX = 0f
                    offsetStart = 0f
                } else {
                    offsetStart = (-((endNum - startNum) / unitNum) * lineSpacing).toFloat()
                    movedX = 0f
                }
                if (listener != null) {
                    listener!!.onNumSelect(selectedNum)
                }
                //计算当前手指放开时的滑动速率
                velocityTracker!!.computeCurrentVelocity(500)
                val velocityX = velocityTracker!!.xVelocity
                if (Math.abs(velocityX) > minVelocityX) {
                    scroller!!.fling(0, 0, velocityX.toInt(), 0, Int.MIN_VALUE, Int.MAX_VALUE, 0, 0)
                }
                postInvalidate()
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller!!.computeScrollOffset()) {
            if (scroller!!.currX == scroller!!.finalX) {
                if (offsetStart + movedX <= 0 && offsetStart + movedX >= -((endNum - startNum) / unitNum) * lineSpacing) {
                    //手指松开时需要磁吸效果
                    offsetStart = offsetStart + movedX
                    movedX = 0f
                    offsetStart = ((offsetStart / lineSpacing).toInt() * lineSpacing).toFloat()
                } else if (offsetStart + movedX > 0) {
                    movedX = 0f
                    offsetStart = 0f
                } else {
                    offsetStart = (-((endNum - startNum) / unitNum) * lineSpacing).toFloat()
                    movedX = 0f
                }
            } else {
                //继续惯性滑动
                movedX = (scroller!!.currX - scroller!!.startY).toFloat()
                //滑动结束:边界控制
                if (offsetStart + movedX > 0) {
                    movedX = 0f
                    offsetStart = 0f
                    scroller!!.forceFinished(true)
                } else if (offsetStart + movedX < -((endNum - startNum) / unitNum) * lineSpacing) {
                    offsetStart = (-((endNum - startNum) / unitNum) * lineSpacing).toFloat()
                    movedX = 0f
                    scroller!!.forceFinished(true)
                }
            }
        } else {
            if (offsetStart + movedX >= 0) {
                offsetStart = 0f
                movedX = 0f
            }
        }
        if (listener != null) {
            listener!!.onNumSelect(selectedNum)
        }
        postInvalidate()
    }

    init {
        initView(context, attrs)
    }
}