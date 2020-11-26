package com.dysen.widgets

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dysen.common.base.XActivity
import com.dysen.common.base_recycler_adapter.SuperRecyclerAdapter
import com.dysen.common.base_recycler_adapter.SuperRecyclerHolder
import com.dysen.widgets.demo.OptionBarViewAty
import com.dysen.widgets.demo.PercentLayoutAty
import com.dysen.widgets.demo.RulerActty
import com.me.optionbarview.OptionBarView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : XActivity() {

    private val menus = mutableListOf("OptionBarView", "PercentLayout", "Ruler")
    private val clzzs =
        mutableListOf<Class<*>>(
            OptionBarViewAty::class.java,
            PercentLayoutAty::class.java,
            RulerActty::class.java
        )

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        initClick()
        initAdapter()
    }

    private fun initClick() {

    }

    private fun initAdapter() {
        rcl.layoutManager = LinearLayoutManager(this)
        rcl.adapter = object : SuperRecyclerAdapter<String>(this, menus) {
            override fun convert(
                holder: SuperRecyclerHolder?,
                t: String?,
                layoutType: Int,
                position: Int
            ) {
                holder?.apply {
                    val opv: OptionBarView = holder?.getViewById(R.id.tv_item) as OptionBarView
                    t?.let {
                        opv.setTitleText(it)
                    }
                    setOnItemClickListenner {
                        if (menus.size == clzzs.size)
                            newInstance(this@MainActivity, clzzs[position])
                        else
                            showLoading("请保证菜单和页面个数对应！")
                    }
                }
            }

            override fun getLayoutAsViewType(t: String?, position: Int): Int {
                return R.layout.layout_common_item
            }

        }
    }


}