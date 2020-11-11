package com.dysen.common.base_recycler_adapter

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * dysen.
 * dy.sen@qq.com    2020/7/13  09:15
 *
 * Info：
 */
object ViewUtils {
    fun closeRefresh(mSrlRefresh: SmartRefreshLayout?) {
        if (mSrlRefresh != null) {
            mSrlRefresh.finishLoadMore()
            mSrlRefresh.finishRefresh()
        }
    }

    fun isFastDoubleClick(view: View?, flag: Boolean) {
        if (view != null) {
            view.isEnabled = flag
        }
    }

    /**
     * 设置view的左上右下图标
     *
     * @param context
     * @param view
     * @param drawableId
     * @param orientationIndex 0 left, 1 top, 2 right, 3 bottom
     */
    fun setViewOrientationBg(
        context: Context,
        view: View?,
        @DrawableRes @ColorRes drawableId: Int,
        orientationIndex: OrientationIndex?
    ) {
        val drawable = context.resources.getDrawable(drawableId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        if (view is TextView) {
            when (orientationIndex) {
                OrientationIndex.LEFT -> view.setCompoundDrawables(drawable, null, null, null)
                OrientationIndex.TOP -> view.setCompoundDrawables(null, drawable, null, null)
                OrientationIndex.RIGHT -> view.setCompoundDrawables(null, null, drawable, null)
                OrientationIndex.BOTTOM -> view.setCompoundDrawables(null, null, null, drawable)
            }
        } else if (view is Button) {
            when (orientationIndex) {
                OrientationIndex.LEFT -> view.setCompoundDrawables(drawable, null, null, null)
                OrientationIndex.TOP -> view.setCompoundDrawables(null, drawable, null, null)
                OrientationIndex.RIGHT -> view.setCompoundDrawables(null, null, drawable, null)
                OrientationIndex.BOTTOM -> view.setCompoundDrawables(null, null, null, drawable)
            }
        } else if (view is EditText) {
            when (orientationIndex) {
                OrientationIndex.LEFT -> view.setCompoundDrawables(drawable, null, null, null)
                OrientationIndex.TOP -> view.setCompoundDrawables(null, drawable, null, null)
                OrientationIndex.RIGHT -> view.setCompoundDrawables(null, null, drawable, null)
                OrientationIndex.BOTTOM -> view.setCompoundDrawables(null, null, null, drawable)
            }
        } else {
        }
    }

    enum class OrientationIndex {
        LEFT, TOP, RIGHT, BOTTOM
    }
}