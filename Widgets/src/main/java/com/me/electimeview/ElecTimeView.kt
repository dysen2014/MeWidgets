package com.me.electimeview

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.me.widgets.R
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author dysen
 * dy.sen@qq.com     11/26/20 2:47 PM
 *
 * Info：
 */
class ElecTimeView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {
    var hour1: ElecTimeNumView
    var hour2: ElecTimeNumView
    var minute1: ElecTimeNumView
    var minute2: ElecTimeNumView
    var second1: ElecTimeNumView
    var second2: ElecTimeNumView
    private var handler: MainHandler

    private class MainHandler(elecTimeView: ElecTimeView) : Handler() {
        private val mWeakReference: WeakReference<ElecTimeView>
        override fun handleMessage(msg: Message) {
            val elecTimeView = mWeakReference.get() ?: return
            if (msg.what == 0x01) {
                val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
                val minute = Calendar.getInstance()[Calendar.MINUTE]
                val second = Calendar.getInstance()[Calendar.SECOND]
                elecTimeView.hour1.setCurNum(hour / 10)
                elecTimeView.hour2.setCurNum(hour % 10)
                elecTimeView.minute1.setCurNum(minute / 10)
                elecTimeView.minute2.setCurNum(minute % 10)
                elecTimeView.second1.setCurNum(second / 10)
                elecTimeView.second2.setCurNum(second % 10)
            }
            elecTimeView.handler.sendEmptyMessageDelayed(0x01, 1000)
        }

        init {
            mWeakReference = WeakReference(elecTimeView)
        }
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_elec_time, this)

        //findView
        hour1 = view.findViewById(R.id.numView1)
        hour2 = view.findViewById(R.id.numView2)
        minute1 = view.findViewById(R.id.numView3)
        minute2 = view.findViewById(R.id.numView4)
        second1 = view.findViewById(R.id.numView5)
        second2 = view.findViewById(R.id.numView6)
        handler = MainHandler(this)
        handler.sendEmptyMessageDelayed(0x01, 1000)
    }
}