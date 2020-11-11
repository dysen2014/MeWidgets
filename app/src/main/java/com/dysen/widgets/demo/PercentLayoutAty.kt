package com.dysen.widgets.demo

import android.os.Bundle
import com.dysen.common.base.XActivity
import com.dysen.widgets.R

class PercentLayoutAty : XActivity() {


    override fun layoutId(): Int {
        return R.layout.activity_percent_layout
    }

    override fun initView(savedInstanceState: Bundle?) {
        initClick()
    }

    private fun initClick() {

    }
}