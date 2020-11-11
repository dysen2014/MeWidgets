package com.dysen.common.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.kongzue.dialog.v3.TipDialog

abstract class XActivity : AppCompatActivity() {
    lateinit var tipDialog: TipDialog
    private var toast: Toast? = null

    companion object {
        fun newInstance(
            aty: AppCompatActivity,
            cls: Class<*>,
            isFinish: Boolean = false
        ) {
            val intent = Intent(aty, cls)
            aty.startActivity(intent)
            if (isFinish)
                aty.finish()
//            aty.startActivityForResult(intent, Constant.ADD_PEOPLE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initView(savedInstanceState)
    }


    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    fun showTip(text: String, duration: Int = Toast.LENGTH_SHORT) {
        if (toast == null)
            toast = Toast.makeText(this, text, duration)

        toast?.apply {
            setText(text)
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    /**
     * 打开等待框
     */
    fun showLoading(message: String, duration: Int = 4000) {
//        tipDialog = showTip(message, type = TipDialog.TYPE.OTHER).setTip(R.mipmap.icon_submit_success)
        tipDialog = TipDialog.showWait(this, message)
        TipDialog.dismiss(duration)
    }

    fun showTipImg(
        message: String,
        type: TipDialog.TYPE = TipDialog.TYPE.WARNING,
        duration: Int = 4000
    ): TipDialog {
        return TipDialog.show(this, message, type).setTipTime(duration)
    }

    /**
     * 关闭等待框
     */
    fun dismissLoading() {
        tipDialog?.setTipTime(200)
    }

}