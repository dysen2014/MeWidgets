package com.dysen.common.base_recycler_adapter

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.text.TextWatcher
import android.util.SparseArray
import android.view.View
import android.view.View.*
import android.widget.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dysen.common.base_recycler_adapter.StringUtils.obtainNoNullText

/**
 *
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/19 - 上午10:38
 * @info 通用的RecyclerView的ViewHolder
 * 采用链式调用
 */
class SuperRecyclerHolder
/**
 * 方法用private，私有化构造方法，限制只允许使用SuperRecyclerHolder.createViewHolder()来创建实例
 */(val context: Context, itemView: View?) : RecyclerView.ViewHolder(
    itemView!!
) {
    private val mViewArray = SparseArray<View?>()
    val itemView: View
        get() = itemView

    fun getViewById(@IdRes viewId: Int): View {
        return retrieveView<View>(viewId)!!
    }

    /**
     * 整个item的点击事件
     */
    fun setOnItemClickListenner(listener: View.OnClickListener?): SuperRecyclerHolder {
        itemView.setOnClickListener(listener)
        return this
    }

    /**
     * 整个item的点击事件，可以根据条件来禁止某些符合条件的点击事件
     *
     *
     * 这个需求是在是开发的时候发现的，才加上去的，能不能点击根据后端的返回来确定的，可以使用该方法
     * 比如：列表显示了很多好友的用户名，在线的可以点击，不在线的不能点击，
     */
    fun setOnItemClickListenner(
        isListener: Boolean,
        listener: View.OnClickListener?
    ): SuperRecyclerHolder {
        itemView.setOnClickListener(if (isListener) listener else null)
        return this
    }

    /**
     * 整个item的长按事件
     */
    fun setOnItemLongClickListener(
        listener: OnLongClickListener?
    ): SuperRecyclerHolder {
        itemView.setOnLongClickListener(listener)
        return this
    }

    fun setOnItemLongClickListener(
        isListener: Boolean,
        listener: OnLongClickListener?
    ): SuperRecyclerHolder {
        itemView.setOnLongClickListener(if (isListener) listener else null)
        return this
    }

    /**
     * 整个item的触摸事件
     */
    fun setOnItemTouchListener(listener: OnTouchListener): SuperRecyclerHolder {
        itemView.setOnTouchListener(listener)
        return this
    }

    fun setOnItemTouchListener(
        isListener: Boolean,
        listener: OnTouchListener
    ): SuperRecyclerHolder {
        itemView.setOnTouchListener(if (isListener) listener else null)
        return this
    }

    fun setOnClickListenner(
        @IdRes viewId: Int,
        listener: View.OnClickListener?
    ): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.setOnClickListener(listener)
        return this
    }

    fun setOnLongClickListener(
        @IdRes viewId: Int,
        listener: OnLongClickListener?
    ): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.setOnLongClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        @IdRes viewId: Int,
        listener: OnTouchListener
    ): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.setOnTouchListener(listener)
        return this
    }

    fun setOnFocusChangeListener(
        @IdRes viewId: Int,
        listener: OnFocusChangeListener
    ): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.onFocusChangeListener = listener
        return this
    }

    fun setOnFocusChangeListener(
        listener: OnFocusChangeListener
    ): SuperRecyclerHolder {
        itemView.onFocusChangeListener = listener
        return this
    }

    fun setAlpha(
        @IdRes viewId: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.alpha = alpha
        return this
    }

    fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes resId: Int): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.setBackgroundResource(resId)
        return this
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.setBackgroundColor(color)
        return this
    }

    fun setClickable(@IdRes viewId: Int, clickable: Boolean): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.isClickable = clickable
        return this
    }

    fun setEnabled(@IdRes viewId: Int, enabled: Boolean): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.isEnabled = enabled
        return this
    }

    fun setFocusable(@IdRes viewId: Int, focusable: Boolean): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.isFocusable = focusable
        return this
    }

    fun setFocusableInTouchMode(
        @IdRes viewId: Int,
        focusableInTouchMode: Boolean
    ): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.isFocusableInTouchMode = focusableInTouchMode
        return this
    }

    fun setTag(@IdRes viewId: Int, tag: Any?): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.tag = tag
        return this
    }

    fun setTag(@IdRes viewId: Int, key: Int, tag: Any?): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.setTag(key, tag)
        return this
    }

    fun setVisibility(@IdRes viewId: Int, visibility: Int): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.visibility = visibility
        return this
    }

    /**
     * 传入是否显示，true显示，false Gone掉
     */
    fun setVisibility(@IdRes viewId: Int, isVisibility: Boolean): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.visibility =
            if (isVisibility) View.VISIBLE else View.GONE
        return this
    }

    fun setLongClickable(@IdRes viewId: Int, longClickable: Boolean): SuperRecyclerHolder {
        retrieveView<View>(viewId)!!.isLongClickable = longClickable
        return this
    }

    /**
     * AppCompatCheckBox, AppCompatCheckedTextView, AppCompatRadioButton,CheckBox, CheckedTextView,
     * CompoundButton, RadioButton, Switch, SwitchCompat, ToggleButton都可以使用
     *
     * 最常使用的是CheckBox，RadioButton,SwitchCompat设置check
     */
    fun setChecked(@IdRes viewId: Int, checked: Boolean): SuperRecyclerHolder {
        val iCheckable: Checkable = retrieveView(viewId)!!
        iCheckable.isChecked = checked
        return this
    }

    /**
     * CheckBox, RadioButton, Switch, SwitchCompat, ToggleButton
     * ,AppCompatCheckBox, AppCompatRadioButton等都可以使用
     * 凡是继承CompoundButton的都可以使用
     *
     * 最常使用的是CheckBox，RadioButton,SwitchCompat设置监听
     */
    fun setOnCheckedChangeListener(
        @IdRes viewId: Int,
        onCheckedChangeListener: CompoundButton.OnCheckedChangeListener?
    ): SuperRecyclerHolder {
        val checkBox = retrieveView<CompoundButton>(viewId)!!
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener)
        return this
    }

    /**
     * 该方法使用频率非常高，而且大多时候，是从网络加载的数据，所有可能会出现空指针异常
     * StringUtils.obtainNoNullText转换以后，确保不会出现空指针异常，网络请求的数据不需要再次进行判空操作
     *
     * 省去每次都要TextUtils.isEmpty操作，不用关心数据是否为空
     */
    fun setText(@IdRes viewId: Int, content: String?): SuperRecyclerHolder {
        return setText(viewId, content, "")
    }

    fun setText(@IdRes viewIds: List<Int>, contents: List<String?>): SuperRecyclerHolder {
        return setText(viewIds, contents, "")
    }

    /**
     * 该方法使用频率非常高，而且大多时候，是从网络加载的数据，所有可能会出现空指针异常
     * StringUtils.obtainNoNullText转换以后，确保不会出现空指针异常，网络请求的数据不需要再次进行判空操作
     *
     * 省去每次都要TextUtils.isEmpty操作，不用关心数据是否为空,可设置默认值
     */
    fun setText(
        @IdRes viewId: Int,
        content: String?,
        defaultContent: String?
    ): SuperRecyclerHolder {
        var content = content
        if (content == null) content = "--"
        val textView = retrieveView<TextView>(viewId)!!
        textView.text = obtainNoNullText(
            content,
            defaultContent!!
        )
        return this
    }

    fun getText(@IdRes viewId: Int): String {
        val textView = retrieveView<TextView>(viewId)!!
        return textView.text.toString()
    }

    fun getText(textView: TextView): String {
        return textView.text.toString()
    }

    fun setText(
        @IdRes viewIds: List<Int>,
        contents: List<String?>,
        defaultContent: String?
    ): SuperRecyclerHolder {
        var count = 0
        var textView: TextView? = null
        count = if (viewIds.size < contents.size) viewIds.size else contents.size
        for (i in 0 until count) {
            textView = retrieveView<TextView>(viewIds[i])
            textView!!.text = obtainNoNullText(
                contents[i],
                defaultContent!!
            )
        }
        return this
    }

    fun setText(@IdRes viewId: Int, @StringRes resId: Int): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.text = resId.toString() + ""
        return this
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setTextColor(color)
        return this
    }

    /**
     * 设置颜色，直接传入colorRes，在方法内部去转换
     */
    fun setTextColorResource(@IdRes viewId: Int, @ColorRes colorRes: Int): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setTextColor(ContextCompat.getColor(context, colorRes))
        return this
    }

    fun setTextSize(@IdRes viewId: Int, size: Float): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.textSize = size
        return this
    }

    fun setTextSize(@IdRes viewId: Int, unit: Int, size: Float): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setTextSize(unit, size)
        return this
    }

    fun setMaxLines(@IdRes viewId: Int, maxLines: Int): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.maxLines = maxLines
        return this
    }

    fun setInputType(@IdRes viewId: Int, type: Int): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.inputType = type
        return this
    }

    fun setHint(@IdRes viewId: Int, @StringRes resId: Int): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setHint(resId)
        return this
    }

    fun setHint(@IdRes viewId: Int, hint: String?): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.hint = hint
        return this
    }

    fun addTextChangedListener(@IdRes viewId: Int, watcher: TextWatcher?): SuperRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.addTextChangedListener(watcher)
        return this
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): SuperRecyclerHolder {
        val imageView = retrieveView<ImageView>(viewId)!!
        imageView.setImageBitmap(bitmap)
        return this
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes resId: Int): SuperRecyclerHolder {
        val imageView = retrieveView<ImageView>(viewId)!!
        imageView.setImageResource(resId)
        return this
    }

    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): SuperRecyclerHolder {
        val view = retrieveView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    /**
     * 保留方法,自己根据项目进行修改
     * 也可以添加设置圆角的图片url等等，根据需求添加
     */
    fun setImageUrl(@IdRes viewId: Int, url: String?): SuperRecyclerHolder {
        if (TextUtils.isEmpty(url)) {
            return this
        }
        val imageView = retrieveView<ImageView>(viewId)!!
        //TODO 请根据自己项目使用的图片加载框架来加载
        return this
    }

    fun setProgress(@IdRes viewId: Int, progress: Int): SuperRecyclerHolder {
        val progressBar = retrieveView<ProgressBar>(viewId)!!
        progressBar.progress = progress
        return this
    }

    fun setProgressMax(@IdRes viewId: Int, max: Int): SuperRecyclerHolder {
        val progressBar = retrieveView<ProgressBar>(viewId)!!
        progressBar.max = max
        return this
    }

    /**
     * 通过viewId从缓存中获取View
     *
     *
     * 对View进行缓存处理
     */
    private fun <T : View?> retrieveView(@IdRes viewId: Int): T? {
        var retrieveView = mViewArray[viewId]
        if (retrieveView == null) {
            retrieveView = itemView.findViewById(viewId)
            mViewArray.put(viewId, retrieveView)
        }
        return retrieveView as T?
    }

    companion object {
        /**
         * 创建ViewHolder实例
         */
        @JvmStatic
        fun createViewHolder(ctx: Context, itemView: View?): SuperRecyclerHolder {
            return SuperRecyclerHolder(ctx, itemView)
        }
    }
}