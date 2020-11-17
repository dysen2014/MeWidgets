package com.me.optionbarview

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Checkable
import com.me.widgets.R

/**
 * dysen.
 * dy.sen@qq.com    11/3/20  10:06 AM
 * <p>
 * Info：
 */
open class OptionBarView(private val mContext: Context, attrs: AttributeSet?) : View(mContext, attrs),
    Checkable {
    /**
     *文本默认字体大小
     */
    private val defTextSize: Float = 14f
    /**
     *图标默认大小
     */
    private val defImageWidth: Float = 24f

    /**
     * 控件的宽
     */
    private var mWidth = 0

    /**
     * 控件的高
     */
    private var mHeight = 0

    /**
     * 左图bitmap
     */
    private var leftImage: Bitmap? = null

    /**
     * 右图bitmap
     */
    private var rightImage: Bitmap? = null

    /**
     * 判断是否显示控件
     */
    private var isShowLeftImg = true
    private var isShowLeftText = true
    private var isShowRightView = true
    private var isShowRightText = true

    //拆分模式(默认是false，也就是一个整体)
    var splitMode = false

    /**
     * 判断按下开始的位置是否在左
     */
    private var leftStartTouchDown = false

    /**
     * 判断按下开始的位置是否在中间
     */
    private var centerStartTouchDown = false

    /**
     * 判断按下开始的位置是否在右
     */
    private var rightStartTouchDown = false

    /**
     * 标题
     */
    private var title = ""

    /**
     * 标题字体大小
     */
    private var titleTextSize: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        defTextSize,
        resources.displayMetrics
    )

    /**
     * 标题颜色
     */
    private var titleTextColor = Color.BLACK

    /**
     * 左边文字
     */
    private var leftText: String? = ""

    /**
     * 左边文字大小
     */
    private var leftTextSize: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        defTextSize,
        resources.displayMetrics
    )

    /**
     * 左字左边距
     */
    private var leftTextMarginLeft = -1

    /**
     * 左图左边距
     */
    private var leftImageMarginLeft = -1

    /**
     * 左图右边距
     */
    private var leftImageMarginRight = -1

    /**
     * 左边文字颜色
     */
    private var leftTextColor = Color.BLACK

    /**
     * 右边文字
     */
    private var rightText: String? = ""

    /**
     * 右边文字大小
     */
    private var rightTextSize: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        defTextSize,
        resources.displayMetrics
    )

    /**
     * 右边文字颜色
     */
    private var rightTextColor = Color.BLACK

    /**
     * 右字右边距
     */
    private var rightTextMarginRight = -1

    /**
     * 右View的左边距
     */
    private var rightViewMarginLeft = -1

    /**
     * 右图的右边距
     */
    private var rightViewMarginRight = -1

    /**
     * 左图的 宽度大小
     */
    private var leftImageWidth = -1
    private var leftImageHeight = -1
    private var rightImageWidth = -1
    private var rightImageHeight = -1
    private val mPaint: Paint

    /**
     * 对文本的约束
     */
    private val mTextBound: Rect

    /**
     * 控制整体布局，可复用绘制其他组件
     */
    private val optionRect: Rect

    /**
     * 是否绘制分隔线
     */
    private var isShowDivideLine = false

    //分割线左、 右边距
    private var divide_line_left_margin = 0
    private var divide_line_right_margin = 0

    //分割线 颜色
    private var divide_line_color = Color.parseColor("#DCDCDC")

    //分割线高度  默认为1px
    private var divide_line_height = 1

    //分割线的位置是否在上方
    private var divide_line_top_gravity = false

    /**
     * 绘制分割线的画笔
     */
    private val dividePaint: Paint

    /**
     * 左区域的右边界
     */
    private var leftBound = 0

    /**
     * 右区域的左边界
     */
    private var rightBound = 0
    var rightViewType = -1

    /**
     * 点击完整的条目的事件回调
     */
    private var optionItemClickListener: OnOptionItemClickListener? = null
    private val paintFlagsDrawFilter =
        PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (rightViewType == RightViewType.SWITCH) {
            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)
            if (widthMode == MeasureSpec.UNSPECIFIED
                || widthMode == MeasureSpec.AT_MOST
            ) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    DEFAULT_SWITCH_BACKGROUND_WIDTH,
                    MeasureSpec.EXACTLY
                )
            }
            if (heightMode == MeasureSpec.UNSPECIFIED
                || heightMode == MeasureSpec.AT_MOST
            ) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    DEFAULT_SWITCH_BACKGROUND_HEIGHT,
                    MeasureSpec.EXACTLY
                )
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //右侧View为 Switch状态
        if (rightViewType == RightViewType.SWITCH) {
            val viewPadding =
                Math.max(switchShadowRadius + switchShadowOffset, switchBorderWidth).toFloat()
            val va: Float =
                if (rightViewMarginRight >= 0) rightViewMarginRight.toFloat() else mWidth.toFloat() / 32
            switchBackgroundRight = w - va - viewPadding
            switchBackgroundLeft = switchBackgroundRight - switchBackgroundWidth + viewPadding
            switchBackgroundTop = (h - switchBackgroundHeight) / 2 + viewPadding
            switchBackgroundBottom = switchBackgroundHeight + switchBackgroundTop - viewPadding
            //计算背景圆弧的半径
            viewRadius = (switchBackgroundBottom - switchBackgroundTop) * .5f

            //按钮的半径
            buttonRadius = viewRadius - switchBorderWidth-5
            centerX = (switchBackgroundLeft + switchBackgroundRight) * .5f
            centerY = (switchBackgroundTop + switchBackgroundBottom) * .5f
            switchButtonMinX = switchBackgroundLeft + viewRadius
            switchButtonMaxX = switchBackgroundRight - viewRadius
            if (isChecked) {
                setCheckedViewState(switchCurrentViewState)
            } else {
                setUncheckViewState(switchCurrentViewState)
            }
            isSwitchInit = true
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mWidth = width
        mHeight = height
        leftBound = 0
        rightBound = Int.MAX_VALUE

        //抗锯齿处理
        canvas.drawFilter = paintFlagsDrawFilter
        optionRect.left = paddingLeft
        optionRect.right = mWidth - paddingRight
        optionRect.top = paddingTop
        optionRect.bottom = mHeight - paddingBottom
        //抗锯齿
        mPaint.isAntiAlias = true
        mPaint.textSize =
            if (titleTextSize > leftTextSize) Math.max(titleTextSize, rightTextSize) else Math.max(
                leftTextSize,
                rightTextSize
            )
        //        mPaint.setTextSize(titleTextSize);
        mPaint.style = Paint.Style.FILL
        //文字水平居中
        mPaint.textAlign = Paint.Align.CENTER

        //计算垂直居中baseline
        val fontMetrics = mPaint.fontMetrics
        val baseLine =
            ((optionRect.bottom + optionRect.top - fontMetrics.bottom - fontMetrics.top) / 2).toInt()
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseline = optionRect.centerY() + distance
        if (title.trim { it <= ' ' } != "") {
            // 正常情况，将字体居中
            mPaint.color = titleTextColor
            canvas.drawText(title, optionRect.centerX().toFloat(), baseline, mPaint)
            optionRect.bottom -= mTextBound.height()
        }
        if (leftImage != null && isShowLeftImg) {
            // 计算左图范围
            optionRect.left = if (leftImageMarginLeft >= 0) leftImageMarginLeft else mWidth / 32
            //计算 左右边界坐标值，若有设置左图偏移则使用，否则使用View的宽度/32
            if (leftImageWidth >= 0) {
                optionRect.right = optionRect.left + leftImageWidth
            } else {
                optionRect.right = optionRect.right + mHeight / 2
            }
            //计算左图 上下边界的坐标值，若无设置右图高度，默认为高度的 1/2
            if (leftImageHeight >= 0) {
                optionRect.top = (mHeight - leftImageHeight) / 2
                optionRect.bottom = leftImageHeight + optionRect.top
            } else {
                optionRect.top = mHeight / 4
                optionRect.bottom = mHeight * 3 / 4
            }
            canvas.drawBitmap(leftImage!!, null, optionRect, mPaint)

            //有左侧图片，更新左区域的边界
            leftBound = Math.max(leftBound, optionRect.right)
        }
        if (rightImage != null && isShowRightView && rightViewType == RightViewType.IMAGE) {
            // 计算右图范围
            //计算 左右边界坐标值，若有设置右图偏移则使用，否则使用View的宽度/32
            optionRect.right =
                mWidth - if (rightViewMarginRight >= 0) rightViewMarginRight else mWidth / 32
            if (rightImageWidth >= 0) {
                optionRect.left = optionRect.right - rightImageWidth
            } else {
                optionRect.left = optionRect.right - mHeight / 2
            }
            //计算右图 上下边界的坐标值，若无设置右图高度，默认为高度的 1/2
            if (rightImageHeight >= 0) {
                optionRect.top = (mHeight - rightImageHeight) / 2
                optionRect.bottom = rightImageHeight + optionRect.top
            } else {
                optionRect.top = mHeight / 4
                optionRect.bottom = mHeight * 3 / 4
            }
            canvas.drawBitmap(rightImage!!, null, optionRect, mPaint)

            //右侧图片，更新右区域边界
            rightBound = Math.min(rightBound, optionRect.left)
        }
        if (leftText != null && leftText != "" && isShowLeftText) {
            mPaint.textSize = leftTextSize
            mPaint.color = leftTextColor
            var w = 0
            if (leftImage != null) {
                w += if (leftImageMarginLeft >= 0) leftImageMarginLeft else mHeight / 8 //增加左图左间距
                w += mHeight / 2 //图宽
                w += if (leftImageMarginRight >= 0) leftImageMarginRight else mWidth / 32 // 增加左图右间距
                w += Math.max(leftTextMarginLeft, 0) //增加左字左间距
            } else {
                w += if (leftTextMarginLeft >= 0) leftTextMarginLeft else mWidth / 32 //增加左字左间距
            }
            mPaint.textAlign = Paint.Align.LEFT
            // 计算了描绘字体需要的范围
            mPaint.getTextBounds(leftText, 0, leftText!!.length, mTextBound)
            canvas.drawText(leftText!!, w.toFloat(), baseline, mPaint)
            //有左侧文字，更新左区域的边界
            leftBound = Math.max(w + mTextBound.width(), leftBound)
        }
        if (rightText != null && rightText != "" && isShowRightText) {
            mPaint.textSize = rightTextSize
            mPaint.color = rightTextColor
            var w = mWidth
            //文字右侧有View
            if (rightViewType != -1) {
                w -= if (rightViewMarginRight >= 0) rightViewMarginRight else mHeight / 8 //增加右图右间距
                w -= if (rightViewMarginLeft >= 0) rightViewMarginLeft else mWidth / 32 //增加右图左间距
                w -= Math.max(rightTextMarginRight, 0) //增加右字右间距
                //扣去右侧View的宽度
                if (rightViewType == RightViewType.IMAGE) {
                    w -= optionRect.right - optionRect.left
                } else if (rightViewType == RightViewType.SWITCH) {
                    w -= (switchBackgroundRight - switchBackgroundLeft + viewRadius * .5f).toInt()
                }
            } else {
                w -= if (rightTextMarginRight >= 0) rightTextMarginRight else mWidth / 32 //增加右字右间距
            }

            // 计算了描绘字体需要的范围
            mPaint.getTextBounds(rightText, 0, rightText!!.length, mTextBound)
            canvas.drawText(rightText!!, w - mTextBound.width().toFloat(), baseline, mPaint)

            //有右侧文字，更新右边区域边界
            rightBound = Math.min(rightBound, w - mTextBound.width())
        }

        //处理分隔线部分
        if (isShowDivideLine) {
            val left = divide_line_left_margin
            val right = mWidth - divide_line_right_margin
            //绘制分割线时，高度默认为 1px
            if (divide_line_height <= 0) {
                divide_line_height = 1
            }
            if (divide_line_top_gravity) {
                val top = 0
                val bottom = divide_line_height
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    dividePaint
                )
            } else {
                val top = mHeight - divide_line_height
                val bottom = mHeight
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    dividePaint
                )
            }
        }

        //判断绘制 Switch
        if (rightViewType == RightViewType.SWITCH && isShowRightView) {
            //边框宽度
            switchBackgroundPaint!!.strokeWidth = switchBorderWidth.toFloat()
            switchBackgroundPaint!!.style = Paint.Style.FILL

            //绘制关闭状态的背景
            switchBackgroundPaint!!.color = uncheckSwitchBackground
            drawRoundRect(
                canvas,
                switchBackgroundLeft,
                switchBackgroundTop,
                switchBackgroundRight,
                switchBackgroundBottom,
                viewRadius,
                switchBackgroundPaint
            )
            //绘制关闭状态的边框
            switchBackgroundPaint!!.style = Paint.Style.STROKE
            switchBackgroundPaint!!.color = uncheckColor
            drawRoundRect(
                canvas,
                switchBackgroundLeft,
                switchBackgroundTop,
                switchBackgroundRight,
                switchBackgroundBottom,
                viewRadius,
                switchBackgroundPaint
            )

            //绘制未选中时的指示器小圆圈
            if (showSwitchIndicator) {
                drawUncheckIndicator(canvas)
            }

            //绘制开启时的背景色
            val des = switchCurrentViewState!!.radius * .5f //[0-backgroundRadius*0.5f]
            switchBackgroundPaint!!.style = Paint.Style.STROKE
            switchBackgroundPaint!!.color = switchCurrentViewState!!.checkStateColor
            switchBackgroundPaint!!.strokeWidth = switchBorderWidth + des * 2f
            drawRoundRect(
                canvas,
                switchBackgroundLeft + des,
                switchBackgroundTop + des,
                switchBackgroundRight - des,
                switchBackgroundBottom - des,
                viewRadius,
                switchBackgroundPaint
            )

            //绘制按钮左边的长条遮挡
            switchBackgroundPaint!!.style = Paint.Style.FILL
            switchBackgroundPaint!!.strokeWidth = 1f
            drawArc(
                canvas,
                switchBackgroundLeft,
                switchBackgroundTop,
                switchBackgroundLeft + 2 * viewRadius,
                switchBackgroundTop + 2 * viewRadius,
                90f,
                180f,
                switchBackgroundPaint
            )
            canvas.drawRect(
                switchBackgroundLeft + viewRadius, switchBackgroundTop,
                switchCurrentViewState!!.buttonX, switchBackgroundTop + 2 * viewRadius,
                switchBackgroundPaint!!
            )

            //绘制Switch的小线条
            if (showSwitchIndicator) {
                drawCheckedIndicator(canvas)
            }

            //绘制Switch的按钮
            drawButton(canvas, switchCurrentViewState!!.buttonX, centerY)

            //更新右侧区域的边界
            rightBound = Math.min(rightBound, switchBackgroundLeft.toInt())
        }

        //视图绘制后，计算 左区域的边界 以及 右区域的边界
        leftBound += 5
        if (rightBound < mWidth / 2) {
            rightBound = mWidth / 2 + 5
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //整体点击模式，不需要判断各区域的点击
        if (!splitMode) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val eventX = event.x.toInt()
                if (eventX <= leftBound) {
                    leftStartTouchDown = true
                } else if (eventX >= rightBound) {
                    rightStartTouchDown = true
                } else {
                    centerStartTouchDown = true
                }
            }
            MotionEvent.ACTION_UP -> {
                val x = event.x.toInt()
                if (leftStartTouchDown && x <= leftBound && optionItemClickListener != null) {
                    optionItemClickListener!!.leftOnClick()
                } else if (rightStartTouchDown && x >= rightBound && optionItemClickListener != null) {
                    optionItemClickListener!!.rightOnClick()
                } else if (centerStartTouchDown && optionItemClickListener != null) {
                    optionItemClickListener!!.centerOnClick()
                }
                leftStartTouchDown = false
                centerStartTouchDown = false
                rightStartTouchDown = false
            }
            else -> {
            }
        }
        /*
         * 当右侧View 为 Switch
         */if (rightViewType == RightViewType.SWITCH) {
            val actionMasked = event.actionMasked
            when (actionMasked) {
                MotionEvent.ACTION_DOWN -> {

                    //只触摸到Switch上判定为即将拖动
                    if (event.x > switchBackgroundLeft && event.x < switchBackgroundRight) {
                        isSwitchTouchingDown = true
                        touchDownTime = System.currentTimeMillis()
                        //取消准备进入拖动状态
                        removeCallbacks(postPendingDrag)
                        //预设100ms进入拖动状态
                        postDelayed(postPendingDrag, 100)
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val eventX = event.x
                    if (isSwitchPendingDragState) {
                        //在准备进入拖动状态过程中，可以拖动按钮位置
                        var fraction =
                            (eventX - switchBackgroundLeft) / (switchBackgroundRight - switchBackgroundLeft)
                        fraction = Math.max(0f, Math.min(1f, fraction))
                        //计算拖动后的圆形按钮的中心x
                        switchCurrentViewState!!.buttonX = (switchButtonMinX
                                + (switchButtonMaxX - switchButtonMinX) * fraction)
                    } else if (isSwitchDragState) {
                        //拖动按钮位置，同时改变对应的背景颜色
                        //计算出滑动的比例？
                        var fraction =
                            (eventX - switchBackgroundLeft) / (switchBackgroundRight - switchBackgroundLeft)
                        fraction = Math.max(0f, Math.min(1f, fraction))
                        switchCurrentViewState!!.buttonX = (switchButtonMinX
                                + (switchButtonMaxX - switchButtonMinX) * fraction)
                        switchCurrentViewState!!.checkStateColor = argbEvaluator.evaluate(
                            fraction,
                            uncheckColor,
                            switchCheckedColor
                        ) as Int
                        postInvalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isSwitchTouchingDown = false
                    //取消准备进入拖动状态
                    removeCallbacks(postPendingDrag)
                    if (System.currentTimeMillis() - touchDownTime <= 350) {
                        //点击时间小于300ms，认为是点击操作
                        toggle()
                    } else if (isSwitchPendingDragState) {
                        //在准备进入拖动状态过程中就抬起了手指，Switch复位
                        pendingCancelDragState()
                    } else if (isSwitchDragState) {
                        //正在拖动状态抬起了手指，计算按钮位置，设置是否切换状态
                        val eventX = event.x
                        var fraction =
                            (eventX - switchBackgroundLeft) / (switchBackgroundRight - switchBackgroundLeft)
                        fraction = Math.max(0f, Math.min(1f, fraction))
                        //是否滑动过了一半
                        val newCheck = fraction > 0.5f
                        if (newCheck == isChecked) {
                            pendingCancelDragState()
                        } else {
                            pendingSettleState(newCheck)
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    isSwitchTouchingDown = false
                    removeCallbacks(postPendingDrag)
                    if (isSwitchPendingDragState || isSwitchDragState) {
                        //复位
                        pendingCancelDragState()
                    }
                }
            }
        }
        return true
    }

    //***********************属性的getter和setter方法*************************************//

    fun setTitleText(stringId: Int) {
        title = mContext.getString(stringId)
        invalidate()
    }
    fun setTitleText(text: String) {
        title = text
        invalidate()
    }

    fun setTitleColor(color: Int) {
        titleTextColor = color
        invalidate()
    }

    fun setTitleSize(sp: Int) {
        titleTextSize = sp2px(sp.toFloat()).toFloat()
        invalidate()
    }

    fun getLeftText(): String? {
        return leftText
    }

    fun setLeftText(text: String?) {
        leftText = text
        invalidate()
    }

    fun setLeftText(stringId: Int) {
        leftText = mContext.getString(stringId)
        invalidate()
    }

    fun setLeftTextColor(color: Int) {
        leftTextColor = color
        invalidate()
    }

    fun setLeftImageMarginRight(dp: Int) {
        leftImageMarginRight = dp2px(dp.toFloat())
        invalidate()
    }

    fun setLeftImageMarginLeft(dp: Int) {
        leftImageMarginLeft = dp2px(dp.toFloat())
        invalidate()
    }

    fun setLeftTextMarginLeft(dp: Int) {
        leftTextMarginLeft = dp2px(dp.toFloat())
        invalidate()
    }

    fun setLeftImage(bitmap: Bitmap?) {
        leftImage = bitmap
        invalidate()
    }

    fun setRightImage(bitmap: Bitmap?) {
        rightImage = bitmap
        invalidate()
    }

    fun setLeftImageWidthHeight(width: Int, Height: Int) {
        leftImageWidth = width
        leftImageHeight = Height
        invalidate()
    }

    fun setRightViewWidthHeight(width: Int, height: Int) {
        if (rightViewType == RightViewType.SWITCH) {
            switchBackgroundWidth = width.toFloat()
            switchBackgroundHeight = height.toFloat()
        }
        if (rightViewType == RightViewType.IMAGE) {
            rightImageWidth = width
            rightImageHeight = height
        }
        invalidate()
    }

    fun setLeftTextSize(sp: Int) {
        leftTextSize = sp2px(sp.toFloat()).toFloat()
        invalidate()
    }

    fun setRightText(text: String?) {
        rightText = text
        invalidate()
    }

    fun setRightText(stringId: Int) {
        rightText = mContext.getString(stringId)
        invalidate()
    }

    fun setRightTextColor(color: Int) {
        rightTextColor = color
        invalidate()
    }

    fun setRightTextSize(sp: Int) {
        leftTextSize = sp2px(sp.toFloat()).toFloat()
        invalidate()
    }

    fun getRightText(): String? {
        return rightText
    }

    fun setRightViewMarginLeft(dp: Int) {
        rightViewMarginLeft = dp2px(dp.toFloat())
        invalidate()
    }

    fun setRightViewMarginRight(dp: Int) {
        rightViewMarginRight = dp2px(dp.toFloat())
        invalidate()
    }

    fun setRightTextMarginRight(dp: Int) {
        rightTextMarginRight = dp2px(dp.toFloat())
        invalidate()
    }

    fun showLeftImg(flag: Boolean) {
        isShowLeftImg = flag
        invalidate()
    }

    fun showLeftText(flag: Boolean) {
        isShowLeftText = flag
        invalidate()
    }

    fun showRightView(flag: Boolean) {
        isShowRightView = flag
        invalidate()
    }

    fun showRightText(flag: Boolean) {
        isShowRightText = flag
        invalidate()
    }

    fun getIsShowDivideLine(): Boolean {
        return isShowDivideLine
    }

    fun setShowDivideLine(showDivideLine: Boolean) {
        isShowDivideLine = showDivideLine
        invalidate()
    }

    fun setDivideLineColor(color: Int) {
        divide_line_color = color
        invalidate()
    }

    //Switch状态监听
    fun setOnSwitchCheckedChangeListener(l: OnSwitchCheckedChangeListener?) {
        onSwitchCheckedChangeListener = l
        invalidate()
    }

    fun setOnOptionItemClickListener(listener: OnOptionItemClickListener?) {
        optionItemClickListener = listener
    }

    //***********************属性的getter和setter方法*************************************//
    //********************************  Switch 部分的方法***********************************//
    //Switch模块的属性
    private val DEFAULT_SWITCH_BACKGROUND_WIDTH = dp2px(45f)
    private val DEFAULT_SWITCH_BACKGROUND_HEIGHT = dp2px(25f)

    /**
     * 背景位置 坐标
     */
    private var switchBackgroundLeft = 0f
    private var switchBackgroundTop = 0f
    private var switchBackgroundRight = 0f
    private var switchBackgroundBottom = 0f
    private var centerX = 0f
    private var centerY = 0f

    /**
     * 阴影半径
     */
    private var switchShadowRadius = 0

    /**
     * 阴影Y偏移px
     */
    private var switchShadowOffset = 0

    /**
     * 阴影颜色
     */
    private var switchShadowColor = 0

    /**
     * 背景半径
     */
    private var viewRadius = 0f

    /**
     * 按钮半径
     */
    private var buttonRadius = 0f

    /**
     * 背景宽的值
     */
    private var switchBackgroundWidth = 0f

    /**
     * 背景高的值
     */
    private var switchBackgroundHeight = 0f

    /**
     * 关闭后的背景底色
     */
    private var uncheckSwitchBackground = 0

    /**
     * 关闭后的背景颜色
     */
    private var uncheckColor = 0

    /**
     * 打开后的背景颜色
     */
    private var switchCheckedColor = 0

    /**
     * 边框宽度px
     */
    private var switchBorderWidth = 0

    /**
     * 打开后的指示线颜色
     */
    private var checkIndicatorLineColor = 0

    /**
     * 打开后的指示线宽度（X轴）
     */
    private var checkIndicatorLineWidth = 0

    /**
     * 打开后的指示线的长度(Y轴)
     */
    private var checkLineLength = 0f

    /**
     * 打开后的指示线X轴的位移
     */
    private var switchCheckedLineOffsetX = 0f

    /**
     * 打开后的指示线Y轴的位移
     */
    private var switchCheckedLineOffsetY = 0f

    /**
     * 关闭后的圆圈的颜色
     */
    private var uncheckSwitchCircleColor = 0

    /**
     * 关闭圆圈线宽
     */
    private var uncheckCircleWidth = 0

    /**
     * 关闭后的圆圈的x轴位移
     */
    private var uncheckCircleOffsetX = 0f

    /**
     * 关闭后的圆圈半径
     */
    private var uncheckSwitchCircleRadius = 0f

    /**
     * 关闭时的圆形按钮的颜色
     */
    private var uncheckButtonColor = 0

    /**
     * 打开时的圆形按钮的颜色
     */
    private var checkedButtonColor = 0

    /**
     * 圆形按钮最左边坐标
     */
    private var switchButtonMinX = 0f

    /**
     * 圆形按钮最右边的坐标
     */
    private var switchButtonMaxX = 0f

    /**
     * 按钮画笔
     */
    private var switchButtonPaint: Paint? = null

    /**
     * Switch的背景画笔
     */
    private var switchBackgroundPaint: Paint? = null

    /**
     * 记录当前状态
     */
    private var switchCurrentViewState: ViewState? = null
    private var beforeState: ViewState? = null
    private var afterState: ViewState? = null
    private var switchBackgroundRect: RectF? = null

    /**
     * 动画状态：
     * 0.静止
     * 1.进入拖动
     * 2.处于拖动
     * 3.拖动-复位
     * 4.拖动-切换
     * 5.点击切换
     */
    private val ANIMATE_STATE_NONE = 0
    private val ANIMATE_STATE_PENDING_DRAG = 1
    private val ANIMATE_STATE_DRAGING = 2
    private val ANIMATE_STATE_PENDING_RESET = 3
    private val ANIMATE_STATE_PENDING_SETTLE = 4
    private val ANIMATE_STATE_SWITCH = 5

    /**
     * 动画状态
     */
    private var animateState = ANIMATE_STATE_NONE

    /**
     * 动画执行器
     */
    private var switchValueAnimator: ValueAnimator? = null
    private val argbEvaluator = ArgbEvaluator()

    /**
     * 是否选中
     */
    private var isSwitchChecked = false

    /**
     * 是否启用动画
     */
    private var enableSwitchAnimate = false

    /**
     * 是否启用阴影效果
     */
    private var switchShadowEffect = false

    /**
     * 是否显示指示器
     */
    private var showSwitchIndicator = false

    /**
     * 是否按下
     */
    private var isSwitchTouchingDown = false

    /**
     *
     */
    private var isSwitchInit = false

    /**
     * Switch是否可以进行状态回调
     */
    private var isSwitchStateChangeCallback = false

    /**
     * 回调接口
     */
    private var onSwitchCheckedChangeListener: OnSwitchCheckedChangeListener? = null

    /**
     * 手势按下的时刻
     */
    private var touchDownTime: Long = 0
    private val postPendingDrag = Runnable {
        if (!isSwitchInAnimating) {
            pendingDragState()
        }
    }

    /**
     * Switch UI的变化核心,更新UI的状态
     */
    private val animatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val value = animation.animatedValue as Float
        when (animateState) {
            ANIMATE_STATE_PENDING_SETTLE, ANIMATE_STATE_PENDING_RESET -> {

                //中间的check线颜色变化
                switchCurrentViewState!!.checkedLineColor = argbEvaluator.evaluate(
                    value,
                    beforeState!!.checkedLineColor,
                    afterState!!.checkedLineColor
                ) as Int
                switchCurrentViewState!!.radius = (beforeState!!.radius
                        + (afterState!!.radius - beforeState!!.radius) * value)
                if (animateState != ANIMATE_STATE_PENDING_DRAG) {
                    switchCurrentViewState!!.buttonX = (beforeState!!.buttonX
                            + (afterState!!.buttonX - beforeState!!.buttonX) * value)
                }
                //已选状态的颜色
                switchCurrentViewState!!.checkStateColor = argbEvaluator.evaluate(
                    value,
                    beforeState!!.checkStateColor,
                    afterState!!.checkStateColor
                ) as Int
            }
            ANIMATE_STATE_SWITCH -> {
                switchCurrentViewState!!.buttonX = (beforeState!!.buttonX
                        + (afterState!!.buttonX - beforeState!!.buttonX) * value)
                val fraction =
                    (switchCurrentViewState!!.buttonX - switchButtonMinX) / (switchButtonMaxX - switchButtonMinX)
                switchCurrentViewState!!.checkStateColor = argbEvaluator.evaluate(
                    fraction,
                    uncheckColor,
                    switchCheckedColor
                ) as Int
                switchCurrentViewState!!.radius = fraction * viewRadius
                switchCurrentViewState!!.checkedLineColor = argbEvaluator.evaluate(
                    fraction,
                    Color.TRANSPARENT,
                    checkIndicatorLineColor
                ) as Int
            }
            ANIMATE_STATE_DRAGING -> {
                Log.i(TAG, "onAnimationUpdate: ANIMATE_STATE_DRAGING")
            }
            ANIMATE_STATE_NONE -> {
                Log.i(TAG, "onAnimationUpdate:  ANIMATE_STATE_NONE ")
            }
            else -> {
            }
        }
        postInvalidate()
    }
    private val switchAnimatorListener: Animator.AnimatorListener =
        object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                when (animateState) {
                    ANIMATE_STATE_PENDING_DRAG -> {
                        animateState = ANIMATE_STATE_DRAGING
                        switchCurrentViewState!!.checkedLineColor = Color.TRANSPARENT
                        switchCurrentViewState!!.radius = viewRadius
                        postInvalidate()
                    }
                    ANIMATE_STATE_PENDING_RESET -> {
                        animateState = ANIMATE_STATE_NONE
                        postInvalidate()
                    }
                    ANIMATE_STATE_PENDING_SETTLE -> {
                        animateState = ANIMATE_STATE_NONE
                        postInvalidate()
                        broadcastEvent()
                    }
                    ANIMATE_STATE_SWITCH -> {
                        isSwitchChecked = !isSwitchChecked
                        animateState = ANIMATE_STATE_NONE
                        postInvalidate()
                        broadcastEvent()
                    }
                    ANIMATE_STATE_NONE -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }

    /**
     * 是否在动画状态
     * @return 是否在动画状态
     */
    private val isSwitchInAnimating: Boolean
        private get() = animateState != ANIMATE_STATE_NONE

    /**
     * 是否在进入拖动或离开拖动状态
     * @return 是否在进入拖动或离开拖动状态
     */
    private val isSwitchPendingDragState: Boolean
        private get() = (animateState == ANIMATE_STATE_PENDING_DRAG
                || animateState == ANIMATE_STATE_PENDING_RESET)

    /**
     * 是否在手指拖动状态
     * @return 是否在手指拖动状态
     */
    private val isSwitchDragState: Boolean
        private get() = animateState == ANIMATE_STATE_DRAGING

    /**
     * 设置是否启用阴影效果
     * @param switchShadowEffect true.启用
     */
    fun setShadowEffect(switchShadowEffect: Boolean) {
        if (this.switchShadowEffect == switchShadowEffect) {
            return
        }
        this.switchShadowEffect = switchShadowEffect
        if (this.switchShadowEffect) {
            switchButtonPaint!!.setShadowLayer(
                switchShadowRadius.toFloat(), 0f, switchShadowOffset.toFloat(),
                switchShadowColor
            )
        } else {
            switchButtonPaint!!.setShadowLayer(
                0f, 0f, 0f,
                0
            )
        }
    }

    fun setEnableEffect(enable: Boolean) {
        enableSwitchAnimate = enable
    }

    /**
     * 开始进入拖动状态
     */
    private fun pendingDragState() {
        if (isSwitchInAnimating) {
            return
        }
        if (!isSwitchTouchingDown) {
            return
        }
        if (switchValueAnimator!!.isRunning) {
            switchValueAnimator!!.cancel()
        }
        animateState = ANIMATE_STATE_PENDING_DRAG
        beforeState!!.copy(switchCurrentViewState)
        afterState!!.copy(switchCurrentViewState)
        if (isChecked) {
            afterState!!.checkStateColor = switchCheckedColor
            afterState!!.buttonX = switchButtonMaxX
            afterState!!.checkedLineColor = switchCheckedColor
        } else {
            afterState!!.checkStateColor = uncheckColor
            afterState!!.buttonX = switchButtonMinX
            afterState!!.radius = viewRadius
        }
        switchValueAnimator!!.start()
    }

    /**
     * 取消拖动状态
     */
    private fun pendingCancelDragState() {
        if (isSwitchDragState || isSwitchPendingDragState) {
            if (switchValueAnimator!!.isRunning) {
                switchValueAnimator!!.cancel()
            }
            animateState = ANIMATE_STATE_PENDING_RESET
            beforeState!!.copy(switchCurrentViewState)

            //开启动画，恢复到原来的状态
            if (isChecked) {
                setCheckedViewState(afterState)
            } else {
                setUncheckViewState(afterState)
            }
            switchValueAnimator!!.start()
        }
    }

    /**
     * 准备切换到新的状态
     */
    private fun pendingSettleState(newCheck: Boolean) {
        if (switchValueAnimator!!.isRunning) {
            switchValueAnimator!!.cancel()
        }
        animateState = ANIMATE_STATE_PENDING_SETTLE
        beforeState!!.copy(switchCurrentViewState)
        isSwitchChecked = newCheck
        if (isChecked) {
            setCheckedViewState(afterState)
        } else {
            setUncheckViewState(afterState)
        }
        switchValueAnimator!!.start()
    }

    /**
     * 切换到指定状态
     * @param checked 状态
     */
    override fun setChecked(checked: Boolean) {
        if (rightViewType == RightViewType.IMAGE) {
            return
        }
        //与当前状态相同则直接刷新
        if (checked == isChecked) {
            postInvalidate()
            return
        }
        //Switch,需要切换状态
        if (rightViewType == RightViewType.SWITCH) {
            toggle(enableSwitchAnimate, false)
        }
    }

    override fun isChecked(): Boolean {
        return if (rightViewType == RightViewType.SWITCH) {
            isSwitchChecked
        } else false
    }

    override fun toggle() {
        if (rightViewType == RightViewType.SWITCH) {
            toggle(true)
        }
    }

    /**
     * 切换到对立的状态
     * @param animate 是否带动画
     */
    fun toggle(animate: Boolean) {
        toggle(animate, true)
    }

    private fun toggle(animate: Boolean, broadcast: Boolean) {
        if (rightViewType != RightViewType.SWITCH) {
            return
        }
        if (!isEnabled) {
            return
        }
        if (isSwitchStateChangeCallback) {
            throw RuntimeException("You should not switch state in [onCheckedChanged]")
        }
        //未初始 Switch UI
        if (!isSwitchInit) {
            isSwitchChecked = !isSwitchChecked
            if (broadcast) {
                broadcastEvent()
            }
            return
        }
        if (switchValueAnimator!!.isRunning) {
            switchValueAnimator!!.cancel()
        }
        if (!enableSwitchAnimate || !animate) {
            isSwitchChecked = !isSwitchChecked
            if (isChecked) {
                setCheckedViewState(switchCurrentViewState)
            } else {
                setUncheckViewState(switchCurrentViewState)
            }
            postInvalidate()
            if (broadcast) {
                broadcastEvent()
            }
            return
        }
        animateState = ANIMATE_STATE_SWITCH
        beforeState!!.copy(switchCurrentViewState)
        if (isChecked) {
            //当前是 checked 则切换到 unchecked
            setUncheckViewState(afterState)
        } else {
            setCheckedViewState(afterState)
        }
        switchValueAnimator!!.start()
    }

    /**
     * ※切换到 非选中状态 ，记录状态值
     * @param switchCurrentViewState
     */
    private fun setUncheckViewState(switchCurrentViewState: ViewState?) {
        switchCurrentViewState!!.radius = 0f
        switchCurrentViewState.checkStateColor = uncheckColor
        switchCurrentViewState.checkedLineColor = Color.TRANSPARENT
        switchCurrentViewState.buttonX = switchButtonMinX
        switchButtonPaint!!.color = uncheckButtonColor
    }

    /**
     * ※切换到 选中状态 ，记录状态
     * @param switchCurrentViewState
     */
    private fun setCheckedViewState(switchCurrentViewState: ViewState?) {
        switchCurrentViewState!!.radius = viewRadius
        switchCurrentViewState.checkStateColor = switchCheckedColor
        switchCurrentViewState.checkedLineColor = checkIndicatorLineColor
        switchCurrentViewState.buttonX = switchButtonMaxX
        switchButtonPaint!!.color = checkedButtonColor
    }

    /**
     * 回调Switch状态改变事件
     */
    private fun broadcastEvent() {
        if (onSwitchCheckedChangeListener != null) {
            isSwitchStateChangeCallback = true
            onSwitchCheckedChangeListener!!.onCheckedChanged(this, isChecked)
        }
        isSwitchStateChangeCallback = false
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(0, 0, 0, 0)
    }
    /**
     * 绘制选中状态指示器
     * @param canvas
     * @param color
     * @param lineWidth
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param switchBackgroundPaint
     */
    /**
     * 绘制选中状态指示器
     * @param canvas
     */
    protected fun drawCheckedIndicator(
        canvas: Canvas,
        color: Int =
            switchCurrentViewState!!.checkedLineColor,
        lineWidth: Float =
            checkIndicatorLineWidth.toFloat(),
        sx: Float =
            switchBackgroundLeft + viewRadius - switchCheckedLineOffsetX,
        sy: Float = centerY - checkLineLength,
        ex: Float =
            switchBackgroundLeft + viewRadius - switchCheckedLineOffsetY,
        ey: Float = centerY + checkLineLength,
        paint: Paint? =
            switchBackgroundPaint
    ) {
        paint!!.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = lineWidth
        canvas.drawLine(sx, sy, ex, ey, paint)
    }

    /**
     * 绘制关闭状态指示器
     * @param canvas
     */
    private fun drawUncheckIndicator(canvas: Canvas) {
        drawUncheckIndicator(
            canvas,
            uncheckSwitchCircleColor,
            uncheckCircleWidth.toFloat(),
            switchBackgroundRight - uncheckCircleOffsetX, centerY,
            uncheckSwitchCircleRadius,
            switchBackgroundPaint
        )
    }

    protected fun drawUncheckIndicator(
        canvas: Canvas,
        color: Int,
        lineWidth: Float,
        centerX: Float, centerY: Float,
        radius: Float,
        paint: Paint?
    ) {
        paint!!.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = lineWidth
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    /**
     * 绘制圆弧
     *
     * @param canvas
     * @param switchBackgroundLeft 背景左顶点坐标
     * @param top
     * @param right
     * @param bottom
     * @param startAngle
     * @param sweepAngle
     * @param paint
     */
    private fun drawArc(
        canvas: Canvas,
        switchBackgroundLeft: Float, top: Float,
        right: Float, bottom: Float,
        startAngle: Float, sweepAngle: Float,
        paint: Paint?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(
                switchBackgroundLeft, top, right, bottom,
                startAngle, sweepAngle, true, paint!!
            )
        } else {
            switchBackgroundRect!![switchBackgroundLeft, top, right] = bottom
            canvas.drawArc(
                switchBackgroundRect!!,
                startAngle, sweepAngle, true, paint!!
            )
        }
    }

    /**
     * 绘制带圆弧的矩形
     * @param canvas
     * @param switchBackgroundLeft
     * @param top
     * @param right
     * @param bottom
     * @param backgroundRadius
     * @param paint
     */
    private fun drawRoundRect(
        canvas: Canvas,
        switchBackgroundLeft: Float, top: Float,
        right: Float, bottom: Float,
        backgroundRadius: Float,
        paint: Paint?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(
                switchBackgroundLeft, top, right, bottom,
                backgroundRadius, backgroundRadius, paint!!
            )
        } else {
            switchBackgroundRect!![switchBackgroundLeft, top, right] = bottom
            canvas.drawRoundRect(
                switchBackgroundRect!!,
                backgroundRadius, backgroundRadius, paint!!
            )
        }
    }

    /**
     * 绘制圆形按钮
     * @param canvas
     * @param x px
     * @param y px
     */
    private fun drawButton(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, buttonRadius, switchButtonPaint!!)
        switchBackgroundPaint?.style = Paint.Style.STROKE
        switchBackgroundPaint?.strokeWidth = 0.5f
        switchBackgroundPaint?.color = -0x222223
        canvas.drawCircle(x, y, buttonRadius, switchBackgroundPaint!!)
    }

    /**
     * 保存Switch的状态属性
     */
    private class ViewState internal constructor() {
        /**
         * 按钮x的坐标[switchButtonMinX-switchButtonMaxX]
         */
        var buttonX = 0f

        /**
         * 状态背景颜色
         */
        var checkStateColor = 0

        /**
         * 选中线的颜色
         */
        var checkedLineColor = 0

        /**
         * 状态背景的半径
         */
        var radius = 0f
        fun copy(source: ViewState?) {
            buttonX = source!!.buttonX
            checkStateColor = source.checkStateColor
            checkedLineColor = source.checkedLineColor
            radius = source.radius
        }
    }
    //********************************  Switch 部分的方法***********************************//
    /**
     * 分区域点击 事件接口
     */
    interface OnOptionItemClickListener {
        fun leftOnClick()
        fun centerOnClick()
        fun rightOnClick()
    }

    /**
     * 分区域点击 事件接口实现类
     */
    abstract class MeOnOptionItemClickListener : OnOptionItemClickListener {
        override fun leftOnClick() {
        }

        override fun centerOnClick() {
        }

        override fun rightOnClick() {
        }

    }

    /*
     * Switch 状态事件改变回调接口
     */
    interface OnSwitchCheckedChangeListener {
        fun onCheckedChanged(view: OptionBarView?, isSwitchChecked: Boolean)
    }

    private fun sp2px(spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, Resources.getSystem().displayMetrics
        ).toInt()
    }

    private fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * 将Vector类型的Drawable转换为Bitmap
     * @param vectorDrawableId vector资源id
     * @return bitmap
     */
    private fun decodeVectorToBitmap(vectorDrawableId: Int): Bitmap? {
        var vectorDrawable: Drawable? = null
        vectorDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContext.getDrawable(vectorDrawableId)
        } else {
            resources.getDrawable(vectorDrawableId)
        }
        if (vectorDrawable != null) {
            //这里若使用Bitmap.Config.RGB565会导致图片资源黑底
            val b = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.minimumHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(b)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return b
        }
        return null
    }

    /**
     * 右侧View的Type
     */
    annotation class RightViewType {
        companion object {
            var IMAGE = 0
            var SWITCH = 1
        }
    }

    companion object {
        private const val TAG = "OptionBarView"
        private fun optInt(
            typedArray: TypedArray?,
            index: Int,
            def: Int
        ): Int {
            return typedArray?.getInt(index, def) ?: def
        }

        private fun optPixelSize(
            typedArray: TypedArray?,
            index: Int,
            def: Float
        ): Float {
            return typedArray?.getDimension(index, def) ?: def
        }

        private fun optPixelSize(
            typedArray: TypedArray?,
            index: Int,
            def: Int
        ): Int {
            return typedArray?.getDimensionPixelOffset(index, def) ?: def
        }

        private fun optColor(
            typedArray: TypedArray?,
            index: Int,
            def: Int
        ): Int {
            return typedArray?.getColor(index, def) ?: def
        }

        private fun optBoolean(
            typedArray: TypedArray?,
            index: Int,
            def: Boolean
        ): Boolean {
            return typedArray?.getBoolean(index, def) ?: def
        }

        private fun optResourceId(
            typedArray: TypedArray?,
            index: Int,
            def: Int
        ): Int {
            return typedArray?.getResourceId(index, def) ?: def
        }

        private fun optString(typedArray: TypedArray?, index: Int, def: String): String {
            if (typedArray == null) {
                return def
            }
            //获取字符串可能为空
            val s = typedArray.getString(index)
            return s ?: def
        }
    }

    init {

        ///////////////////////////////////////////////////////////////////////////////////////////////
        //                                                                                           //
        //                         获取自定义属性的值                                                   //
        //                                                                                           //
        ///////////////////////////////////////////////////////////////////////////////////////////////
        val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.OptionBarView)
        isShowDivideLine = optBoolean(typedArray, R.styleable.OptionBarView_show_divide_line, false)
        divide_line_top_gravity =
            optBoolean(typedArray, R.styleable.OptionBarView_divide_line_top_gravity, false)
        divide_line_color = optColor(
            typedArray,
            R.styleable.OptionBarView_divide_line_color,
            Color.parseColor("#DCDCDC")
        )
        divide_line_height =
            optPixelSize(typedArray, R.styleable.OptionBarView_divide_line_height, 1)
        divide_line_left_margin =
            optPixelSize(typedArray, R.styleable.OptionBarView_divide_line_left_margin, dp2px(0f))
        divide_line_right_margin =
            optPixelSize(typedArray, R.styleable.OptionBarView_divide_line_right_margin, dp2px(0f))
        leftText = optString(typedArray, R.styleable.OptionBarView_left_text, "")
        rightText = optString(typedArray, R.styleable.OptionBarView_right_text, "")
        title = optString(typedArray, R.styleable.OptionBarView_title, "")
        titleTextSize =
            optPixelSize(typedArray, R.styleable.OptionBarView_title_size, sp2px(-1f)).toFloat()
        titleTextColor = optColor(typedArray, R.styleable.OptionBarView_title_color, Color.BLACK)
        leftTextMarginLeft =
            optPixelSize(typedArray, R.styleable.OptionBarView_left_text_margin_left, dp2px(-1f))
        rightTextMarginRight =
            optPixelSize(typedArray, R.styleable.OptionBarView_right_text_margin_right, dp2px(-1f))
        leftImageWidth =
            optPixelSize(typedArray, R.styleable.OptionBarView_left_src_height, dp2px(defImageWidth))
        rightImageWidth =
            optPixelSize(typedArray, R.styleable.OptionBarView_right_src_height, dp2px(defImageWidth))
        leftImageHeight =
            optPixelSize(typedArray, R.styleable.OptionBarView_left_src_width, dp2px(defImageWidth))
        rightImageHeight =
            optPixelSize(typedArray, R.styleable.OptionBarView_right_src_width, dp2px(defImageWidth))
        rightTextSize = optPixelSize(
            typedArray,
            R.styleable.OptionBarView_right_text_size,
            sp2px(defTextSize)
        ).toFloat()
        leftTextSize =
            optPixelSize(
                typedArray,
                R.styleable.OptionBarView_left_text_size,
                sp2px(defTextSize)
            ).toFloat()
        leftImageMarginLeft =
            optPixelSize(typedArray, R.styleable.OptionBarView_left_image_margin_left, dp2px(-1f))
        rightViewMarginLeft =
            optPixelSize(typedArray, R.styleable.OptionBarView_right_view_margin_left, dp2px(-1f))
        leftImageMarginRight =
            optPixelSize(typedArray, R.styleable.OptionBarView_left_image_margin_right, dp2px(-1f))
        rightViewMarginRight =
            optPixelSize(typedArray, R.styleable.OptionBarView_right_view_margin_right, dp2px(-1f))
        leftTextColor = optColor(typedArray, R.styleable.OptionBarView_left_text_color, Color.BLACK)
        rightTextColor =
            optColor(typedArray, R.styleable.OptionBarView_right_text_color, Color.BLACK)
        splitMode = optBoolean(typedArray, R.styleable.OptionBarView_split_mode, false)
        rightViewType = optInt(typedArray, R.styleable.OptionBarView_rightViewType, -1)
        val leftImageId = optResourceId(typedArray, R.styleable.OptionBarView_left_src, 0)
        if (leftImageId != 0) {
            leftImage = BitmapFactory.decodeResource(resources, leftImageId)
            //需要加载Vector资源
            if (leftImage == null) {
                val vectorBitmap = decodeVectorToBitmap(leftImageId)
                if (vectorBitmap != null) {
                    leftImage = vectorBitmap
                }
            }
        }
        if (rightViewType == RightViewType.IMAGE) {
            //获取定义的右侧图片ResId属性
            val rightImageId = optResourceId(typedArray, R.styleable.OptionBarView_right_src, 0)
            if (rightImageId != 0) {
                rightImage = BitmapFactory.decodeResource(resources, rightImageId)
                //需要加载Vector资源
                if (rightImage == null) {
                    val vectorBitmap = decodeVectorToBitmap(
                        optResourceId(
                            typedArray,
                            R.styleable.OptionBarView_right_src,
                            0
                        )
                    )
                    if (vectorBitmap != null) {
                        rightImage = vectorBitmap
                    }
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////
        //                                                                                           //
        //                          Switch 获取初始参数                                                //
        //                                                                                           //
        ///////////////////////////////////////////////////////////////////////////////////////////////
        if (rightViewType == RightViewType.SWITCH) {
            switchBackgroundWidth = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_background_width,
                dp2px(50f)
            ).toFloat()
            switchBackgroundHeight = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_background_height,
                dp2px(32f
            )
            ).toFloat()
            switchShadowEffect = optBoolean(
                typedArray,
                R.styleable.OptionBarView_switch_shadow_effect,
                true
            )
            uncheckSwitchCircleColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_uncheckcircle_color,
                -0x555556
            )
            uncheckCircleWidth = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_uncheckcircle_width,
                dp2px(1f)//小圆的环形宽度
            )
            uncheckCircleOffsetX = dp2px(10f).toFloat()
            uncheckSwitchCircleRadius = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_uncheckcircle_radius,
                dp2px(4f)
            ).toFloat() //dp2px(4);
            switchCheckedLineOffsetX = dp2px(4f).toFloat()
            switchCheckedLineOffsetY = dp2px(4f).toFloat()
            switchShadowRadius = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_shadow_radius,
                dp2px(2.5f)
            ) //dp2px(2.5f);
            switchShadowOffset = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_shadow_offset,
                dp2px(1.5f)
            ) //dp2px(1.5f);
            switchShadowColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_shadow_color,
                0X33000000
            ) //0X33000000;
            uncheckColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_uncheck_color,
                -0x222223
            ) //0XffDDDDDD;
            switchCheckedColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_checked_color,
                Color.parseColor("#66BB6A")
            ) //0Xff51d367;
            switchBorderWidth = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_border_width,
                dp2px(0.7f)
            ) //dp2px(1);
            checkIndicatorLineColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_checkline_color,
                Color.WHITE
            ) //Color.TRANSPARENT;
            checkIndicatorLineWidth = optPixelSize(
                typedArray,
                R.styleable.OptionBarView_switch_checkline_width,
                dp2px(1f)
            ) //dp2px(1.0f);
            checkLineLength = dp2px(6f).toFloat()
            val buttonColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_button_color,
                Color.WHITE
            ) //Color.WHITE;
            //未选和已选的圆形按钮颜色默认情况都是白色
            uncheckButtonColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_uncheckbutton_color,
                buttonColor
            )
            checkedButtonColor = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_checkedbutton_color,
                buttonColor
            )
            val effectDuration = optInt(
                typedArray,
                R.styleable.OptionBarView_switch_effect_duration,
                300
            ) //300;
            isSwitchChecked = optBoolean(
                typedArray,
                R.styleable.OptionBarView_switch_checked,
                false
            )
            showSwitchIndicator = optBoolean(
                typedArray,
                R.styleable.OptionBarView_switch_show_indicator,
                true
            )
            uncheckSwitchBackground = optColor(
                typedArray,
                R.styleable.OptionBarView_switch_background,
                Color.parseColor("#EEEEEE")
            ) //Color.WHITE;
            enableSwitchAnimate = optBoolean(
                typedArray,
                R.styleable.OptionBarView_switch_enable_effect,
                true
            )
            switchBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            switchButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            switchButtonPaint?.color = buttonColor
            if (switchShadowEffect) {
                switchButtonPaint?.setShadowLayer(
                    switchShadowRadius.toFloat(), 0f, switchShadowOffset.toFloat(),
                    switchShadowColor
                )
            }
            //创建一个Switch的背景的圆角矩形
            switchBackgroundRect = RectF()

            //设置切换前后状态动画记录变量
            switchCurrentViewState = ViewState()
            beforeState = ViewState()
            afterState = ViewState()

            //初始化动画执行器
            switchValueAnimator = ValueAnimator.ofFloat(0f, 1f)
            switchValueAnimator?.duration = effectDuration.toLong()
            switchValueAnimator?.repeatCount = 0
            switchValueAnimator?.addUpdateListener(animatorUpdateListener)
            switchValueAnimator?.addListener(switchAnimatorListener)
            super.setClickable(true)
            setPadding(0, 0, 0, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setLayerType(LAYER_TYPE_SOFTWARE, null)
            }
        }

        //参数获取完毕后 回收typeArray
        typedArray.recycle()
        optionRect = Rect()
        mPaint = Paint()
        mTextBound = Rect()
        // 计算了描绘字体需要的范围
        mPaint.getTextBounds(title, 0, title.length, mTextBound)

        //右侧图片非空则设置右侧为图片类型
        if (rightImage != null) {
            rightViewType = RightViewType.IMAGE
        }

        //初始化 分割线 画笔
        dividePaint = Paint()
        dividePaint.color = divide_line_color
    }
}