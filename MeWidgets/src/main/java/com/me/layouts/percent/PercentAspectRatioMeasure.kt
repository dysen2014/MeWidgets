package com.me.layouts.percent

import android.view.View
import android.view.ViewGroup

/**
 * dysen.
 * dy.sen@qq.com    11/3/20  10:06 AM
 *
 *
 * Info：
 */
object PercentAspectRatioMeasure {
    /**
     * Updates the given measure spec with respect to the aspect ratio.
     *
     *
     * Note: Measure spec is not changed if the aspect ratio is not greater than zero or if
     * layoutParams is null.
     *
     *
     * Measure spec of the layout dimension (width or height) specified as "0dp" is updated
     * to match the measure spec of the other dimension adjusted by the aspect ratio. Exactly one
     * layout dimension should be specified as "0dp".
     *
     *
     * Padding is taken into account so that the aspect ratio refers to the content without
     * padding: `aspectRatio == (viewWidth - widthPadding) / (viewHeight - heightPadding)`
     *
     *
     * Updated measure spec respects the parent's constraints. I.e. measure spec is not changed
     * if the parent has specified mode `EXACTLY`, and it doesn't exceed measure size if parent
     * has specified mode `AT_MOST`.
     *
     * @param spec in/out measure spec to be updated
     * @param aspectRatio desired aspect ratio
     * @param layoutParams view's layout params
     * @param widthPadding view's left + right padding
     * @param heightPadding view's top + bottom padding
     */
    @JvmStatic
    fun updateMeasureSpec(
        spec: Spec,
        aspectRatio: Float,
        layoutParams: ViewGroup.LayoutParams?,
        widthPadding: Int,
        heightPadding: Int
    ) {
        if (aspectRatio <= 0 || layoutParams == null) {
            return
        }
        if (shouldAdjust(layoutParams.height)) {
            val widthSpecSize = View.MeasureSpec.getSize(spec.width)
            val desiredHeight =
                ((widthSpecSize - widthPadding) / aspectRatio + heightPadding).toInt()
            val resolvedHeight = View.resolveSize(desiredHeight, spec.height)
            spec.height = View.MeasureSpec.makeMeasureSpec(resolvedHeight, View.MeasureSpec.EXACTLY)
        } else if (shouldAdjust(layoutParams.width)) {
            val heightSpecSize = View.MeasureSpec.getSize(spec.height)
            val desiredWidth =
                ((heightSpecSize - heightPadding) * aspectRatio + widthPadding).toInt()
            val resolvedWidth = View.resolveSize(desiredWidth, spec.width)
            spec.width = View.MeasureSpec.makeMeasureSpec(resolvedWidth, View.MeasureSpec.EXACTLY)
        }
    }

    private fun shouldAdjust(layoutDimension: Int): Boolean {
        // Note: wrap_content is supported for backwards compatibility, but should not be used.
        return layoutDimension == 0 || layoutDimension == ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * Holder for width and height measure specs.
     */
    class Spec {
        @JvmField
        var width = 0
        @JvmField
        var height = 0
    }
}