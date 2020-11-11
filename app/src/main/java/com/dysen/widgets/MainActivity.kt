package com.dysen.widgets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.me.optionbarview.OptionBarView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    private fun showTip(text: String, duration: Int = Toast.LENGTH_SHORT) {
        if (toast == null)
            toast = Toast.makeText(this, text, duration)

        toast?.apply {
            setText(text)
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }


    }
}