package com.dysen.widgets.demo

import android.os.Bundle
import com.dysen.common.base.XActivity
import com.dysen.widgets.R
import com.me.optionbarview.OptionBarView
import kotlinx.android.synthetic.main.activity_option_bar_view.*

class OptionBarViewAty : XActivity() {


    override fun layoutId(): Int = R.layout.activity_option_bar_view

    override fun initView(savedInstanceState: Bundle?) {
        initClick()
    }


    private fun initClick() {
        //区域分割 响应区域点击事件
        opv.splitMode = true
        opv.setOnOptionItemClickListener(object : OptionBarView.OnOptionItemClickListener {
            override fun leftOnClick() {
                showTip("leftText")
            }

            override fun centerOnClick() {
                showTip("titleText")
            }

            override fun rightOnClick() {
                showTip("rightText")
            }

        })
    }
}