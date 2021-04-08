package com.dysen.widgets.demo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.dysen.common.base.XActivity
import com.dysen.widgets.R
import com.me.signature_view.SignatureView

/**
* @author dysen
* dy.sen@qq.com     4/7/21 11:25 AM
*
* Info：在线签名测试类
*/
class SignatureViewActivity : XActivity() {
    private var base64: String? = null

    private val signatureView: SignatureView by lazy { findViewById(R.id.sv) }
    private val mIvShow: ImageView by lazy { findViewById(R.id.mIvShow) }

    override fun layoutId(): Int = R.layout.activity_signature_view

    override fun initView(savedInstanceState: Bundle?) {
        initData()
    }

    private fun initData() {

    }

    fun confirm(view: View) {
        base64 = signatureView.mBitmap?.let { SignatureView.saveBitmapToBase64(it) }
        mIvShow.setImageBitmap(signatureView.mBitmap)
    }

    fun reset(view: View) {
        mIvShow.setImageBitmap(null)
        signatureView.reset()
    }
}