package com.dysen.common.base_recycler_adapter

import com.dysen.widgets.R
import java.util.*

/**
 * @author dysen
 * dy.sen@qq.com     12/2/20 3:14 PM
 *
 *
 * Info：
 */
abstract class MeAdapter<T> : SuperRecyclerAdapter<T> {
    private var layoutResId: Int
    private var mValueList: List<T> = ArrayList()

    constructor(layoutResId: Int) {
        this.layoutResId = layoutResId
        setDatas(mValueList)
    }

    constructor(layoutResId: Int, valueList: List<T>) {
        mValueList = valueList
        this.layoutResId = layoutResId
        setDatas(mValueList)
    }

    override fun setDatas(items: List<T>?) {
//        refreshList(items);
        super.setDatas(items)
    }

    override fun onBindViewHolder(holder: SuperRecyclerHolder, position: Int) {

//        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.getContext(), R.anim.trans_scale_alpha));
        super.onBindViewHolder(holder, position)
    }

    private fun layoutResId(layoutResId: Int): Int {
        return if (mValueList.isEmpty()) R.layout.layout_common_empty else layoutResId
    }

    private fun refreshList(mValueList: List<T>) {
//        Tools.setIsVisible(MeRecyclerView.emptyLayout, mValueList?.isEmpty() ?: true)
//        Tools.setIsVisible(MeRecyclerView.swipeRecyclerView, mValueList?.isNotEmpty() ?: false)
    }

    override fun convert(holder: SuperRecyclerHolder?, o: T?, layoutType: Int, position: Int) {}
    override fun getLayoutAsViewType(o: T?, position: Int): Int {
        return layoutResId
    }
}