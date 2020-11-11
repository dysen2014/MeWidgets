package com.me.layouts.percent

import android.content.res.TypedArray
import android.graphics.*
import android.view.View
import com.me.widgets.R

/**
 * dysen.
 * dy.sen@qq.com    11/10/20  14:06 PM
 *
 *
 * Info：
 */
class ClipHelper {
    private val radii = FloatArray(8) // top-left, top-right, bottom-right, bottom-left
    private var mClipPath // 剪裁区域路径
            : Path? = null
    private var mStrokePath // 描边区域路径
            : Path? = null
    private var mPaint // 画笔
            : Paint? = null
    private var mRoundAsCircle = false // 圆形
    private var mStrokeColor // 描边颜色
            = 0
    private var mStrokeWidth // 描边半径
            = 0
    private var mAreaRegion // 内容区域
            : Region? = null
    private var mEnableClip // 是否开启裁剪，使用必须置为true,以提高性能
            = false

    fun getmAreaRegion(): Region? {
        return mAreaRegion
    }

    fun initClipData(typedArray: TypedArray): Boolean {
        mEnableClip = typedArray.getBoolean(R.styleable.PercentLayout_Layout_enable_clip, false)
        if (!mEnableClip) return false
        mRoundAsCircle =
            typedArray.getBoolean(R.styleable.PercentLayout_Layout_round_as_circle, false)
        mRoundAsCircle =
            typedArray.getBoolean(R.styleable.PercentLayout_Layout_round_as_circle, false)
        mStrokeColor =
            typedArray.getColor(R.styleable.PercentLayout_Layout_layout_stroke_color, Color.WHITE)
        mStrokeWidth = typedArray.getDimensionPixelSize(
            R.styleable.PercentLayout_Layout_layout_stroke_width,
            0
        )
        val roundCorner =
            typedArray.getDimensionPixelSize(R.styleable.PercentLayout_Layout_round_corner, 0)
        val roundCornerTopLeft = typedArray.getDimensionPixelSize(
            R.styleable.PercentLayout_Layout_round_corner_top_left, roundCorner
        )
        val roundCornerTopRight = typedArray.getDimensionPixelSize(
            R.styleable.PercentLayout_Layout_round_corner_top_right, roundCorner
        )
        val roundCornerBottomLeft = typedArray.getDimensionPixelSize(
            R.styleable.PercentLayout_Layout_round_corner_bottom_left, roundCorner
        )
        val roundCornerBottomRight = typedArray.getDimensionPixelSize(
            R.styleable.PercentLayout_Layout_round_corner_bottom_right, roundCorner
        )
        radii[0] = roundCornerTopLeft.toFloat()
        radii[1] = roundCornerTopLeft.toFloat()
        radii[2] = roundCornerTopRight.toFloat()
        radii[3] = roundCornerTopRight.toFloat()
        radii[4] = roundCornerBottomRight.toFloat()
        radii[5] = roundCornerBottomRight.toFloat()
        radii[6] = roundCornerBottomLeft.toFloat()
        radii[7] = roundCornerBottomLeft.toFloat()
        mClipPath = Path()
        mStrokePath = Path()
        mAreaRegion = Region()
        mPaint = Paint()
        mPaint!!.color = Color.WHITE
        mPaint!!.isAntiAlias = true
        return mEnableClip
    }

    fun onSizeChange(view: View, w: Int, h: Int) {
        val areas = RectF()
        areas.left = view.paddingLeft.toFloat()
        areas.top = view.paddingTop.toFloat()
        areas.right = w - view.paddingRight.toFloat()
        areas.bottom = h - view.paddingBottom.toFloat()
        mClipPath!!.reset()
        if (mRoundAsCircle) {
            val d = if (areas.width() >= areas.height()) areas.height() else areas.width()
            val r = d / 2
            val center = PointF((w / 2).toFloat(), (h / 2).toFloat())
            mClipPath!!.fillType = Path.FillType.INVERSE_EVEN_ODD
            mClipPath!!.addRect(areas, Path.Direction.CW)
            mClipPath!!.addCircle(center.x, center.y, r - 0.5f, Path.Direction.CW)
            mStrokePath!!.addCircle(center.x, center.y, r, Path.Direction.CW)
        } else {
            mClipPath!!.fillType = Path.FillType.EVEN_ODD
            mClipPath!!.addRoundRect(areas, radii, Path.Direction.CW)
            mStrokePath!!.addRoundRect(areas, radii, Path.Direction.CW)
        }
        val clip = Region(
            areas.left.toInt(), areas.top.toInt(),
            areas.right.toInt(), areas.bottom.toInt()
        )
        mAreaRegion!!.setPath(mStrokePath!!, clip)
    }

    fun dispatchDraw(canvas: Canvas) {
        if (mStrokeWidth > 0) {
            mPaint!!.xfermode = null
            mPaint!!.strokeWidth = mStrokeWidth * 2.toFloat()
            mPaint!!.color = mStrokeColor
            mPaint!!.style = Paint.Style.STROKE
            canvas.drawPath(mStrokePath!!, mPaint!!)
        }
        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        mPaint!!.strokeWidth = 0f
        mPaint!!.style = Paint.Style.FILL
        canvas.drawPath(mClipPath!!, mPaint!!)
        canvas.restore()
    }
}