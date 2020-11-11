package com.dysen.common.base_recycler_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * dysen.
 * dy.sen@qq.com    2020/7/13  09:15

 * Info：
 */
public class ViewUtils {

    public static void closeRefresh(SmartRefreshLayout mSrlRefresh) {
        if (mSrlRefresh != null) {
            mSrlRefresh.finishLoadMore();
            mSrlRefresh.finishRefresh();
        }

    }

    public static void isFastDoubleClick(View view, boolean flag) {
        if (view != null) {
            view.setEnabled(flag);
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
    public static void setViewOrientationBg(Context context, View view, @DrawableRes @ColorRes int drawableId, OrientationIndex orientationIndex) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (view instanceof TextView) {
            switch (orientationIndex) {
                case LEFT:
                    ((TextView) view).setCompoundDrawables(drawable, null, null, null);
                    break;
                case TOP:
                    ((TextView) view).setCompoundDrawables(null, drawable, null, null);
                    break;
                case RIGHT:
                    ((TextView) view).setCompoundDrawables(null, null, drawable, null);
                    break;
                case BOTTOM:
                    ((TextView) view).setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        } else if (view instanceof Button) {
            switch (orientationIndex) {
                case LEFT:
                    ((Button) view).setCompoundDrawables(drawable, null, null, null);
                    break;
                case TOP:
                    ((Button) view).setCompoundDrawables(null, drawable, null, null);
                    break;
                case RIGHT:
                    ((Button) view).setCompoundDrawables(null, null, drawable, null);
                    break;
                case BOTTOM:
                    ((Button) view).setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        } else if (view instanceof EditText) {
            switch (orientationIndex) {
                case LEFT:
                    ((EditText) view).setCompoundDrawables(drawable, null, null, null);
                    break;
                case TOP:
                    ((EditText) view).setCompoundDrawables(null, drawable, null, null);
                    break;
                case RIGHT:
                    ((EditText) view).setCompoundDrawables(null, null, drawable, null);
                    break;
                case BOTTOM:
                    ((EditText) view).setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        } else {

        }
    }

    public static enum OrientationIndex{
        LEFT,TOP,RIGHT,BOTTOM
    }
}
