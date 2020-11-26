package com.dysen.widgets.demo

import android.os.Bundle
import com.dysen.common.base.XActivity
import com.dysen.widgets.R
import com.me.rulerview.RulerView
import kotlinx.android.synthetic.main.activity_ruler.*

/**
 * @author dysen
 * dy.sen@qq.com     11/26/20 3:27 PM
 *
 * Info：尺子选择器
 */
class RulerActty : XActivity() {

    override fun layoutId(): Int = R.layout.activity_ruler

    override fun initView(savedInstanceState: Bundle?) {
        RulerView.endNum = 100
        rulerView.run {
            rulerView.setOnNumSelectListener(object : RulerView.OnNumSelectListener {
                override fun onNumSelect(selectedNum: Int) {
                    tvNum.text = "$selectedNum cm"
                    tvNum.setTextColor(RulerView.indicatorColor)
                }
            })
        }
    }
}