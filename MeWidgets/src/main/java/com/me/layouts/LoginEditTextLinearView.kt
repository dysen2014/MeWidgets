package com.me.layouts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.LinearLayout

/**
 * dysen.
 * dy.sen@qq.com    11/6/20  10:06 AM
 *
 *
 * Info： 输入框获取底部边框变色
 */
class LoginEditTextLinearView : LinearLayout {
    private var mCanvas: Canvas? = null
    private var mContext: Context
    private var editText: EditText? = null
    private var viewHeight = 0
    private var viewWidth = 0
    private var type = 0

    constructor(context: Context) : super(context) {
        mContext = context
        setWillNotDraw(false)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        mContext = context
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mCanvas = canvas
        viewHeight = height
        viewWidth = width
        if (type == 2) {
            bottomLine2()
        } else {
            bottomLine1()
        }
    }

    private fun bottomLine1() {
        if (mCanvas != null) {
            val mPaint = Paint()
            mPaint.isAntiAlias = true
            mPaint.style = Paint.Style.STROKE
            mPaint.color = Color.parseColor("#D5D5D5")
            mPaint.strokeWidth = 6f
            mCanvas!!.drawLine(
                0f,
                viewHeight.toFloat(),
                viewWidth.toFloat(),
                viewHeight.toFloat(),
                mPaint
            )
        }
    }

    private fun bottomLine2() {
        if (mCanvas != null) {
            val mPaint = Paint()
            mPaint.isAntiAlias = true
            mPaint.style = Paint.Style.STROKE
            mPaint.color = Color.parseColor("#E91C41")
            mPaint.strokeWidth = 10f
            mCanvas!!.drawLine(
                0f,
                viewHeight.toFloat(),
                viewWidth.toFloat(),
                viewHeight.toFloat(),
                mPaint
            )
        }
    }

    fun setEditText(editText: EditText) {
        this.editText = editText
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) { // 此处为得到焦点时的处理内容
                type = 2
                postInvalidate()
            } else { // 此处为失去焦点时的处理内容
                type = 1
                postInvalidate()
            }
        }
    }
}